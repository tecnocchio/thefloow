/**
 * 
 */
package info.tecnocchio.theFloow.db.mongo;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;

import info.tecnocchio.theFloow.db.DbConnection;

/**
 * @author maurizio
 *
 */
public class MongoConnection implements DbConnection {

	private MongoClient mongo;
	private MongoDatabase db;

	@Override
	public void close() {
		mongo.close();

	}

	@Override
	public boolean connect(String mongoHost, Integer mongoPort, String name, String user, String pwd) {
		mongo = new MongoClient(mongoHost, mongoPort == null ? 27017 : mongoPort);
		
		db = mongo.getDatabase(name);
		return true;
	}

	@Override
	public void checkDbStructure(String fileName) {
		Document confDoc = db.getCollection(TABLE_CONFIG).find(Filters.exists(CONFIG_STATUS_PROP_FIELD)).first();
		if (confDoc != null)		return ;
		initDbConfig();
		return ;

	}

	private void initDbConfig() {
		
		MongoCollection<Document> collection = db.getCollection(TABLE_CONFIG);
		Document document = new Document();
		document.put(CONFIG_STATUS_PROP_FIELD, Status.START.name());
		document.put(CONFIG_FINALCHUNK_PROP_FIELD, null);
		document.put(CONFIG_CREATED_PROP_FIELD, new Date());
		collection.insertOne(document);
		db.getCollection(TABLE_WORDS).drop();
		db.getCollection(TABLE_CHUNKS).drop();
		db.getCollection(TABLE_WORDS).createIndex(Indexes.ascending(WORD_WORDNAME_PROP_FIELD),
				new IndexOptions().unique(true));
		db.getCollection(TABLE_WORDS).createIndex(Indexes.ascending(WORD_COUNT_PROP_FIELD));

	}

	@Override
	public Long getNextChunkToWork(Long currentChunk) {

		Long finalChunk = getFinalChunk();// check if someone else discovered final


		if (currentChunk == null)
			currentChunk = -1L;
		Document cnkDoc = db.getCollection(TABLE_CHUNKS).find(Filters.eq(CHUNK_NUM_PROP_FIELD, currentChunk + 1))
				.first();
		// next chunk is free to work and is not final chunk
		if (cnkDoc == null && (finalChunk == null || finalChunk > (currentChunk + 1))) {
			createNewChunk(currentChunk + 1);
			return currentChunk + 1;
		}
		// search for bigger chunk in progress
		Document lastChunk = db.getCollection(TABLE_CHUNKS).find()
				.sort(new Document(CHUNK_NUM_PROP_FIELD, DbConnection.COMMON)).first();
		long cnkNum = lastChunk.getLong(CHUNK_NUM_PROP_FIELD) + 1L;
		if (finalChunk == null || cnkNum <= finalChunk) {
			createNewChunk(cnkNum);
			return cnkNum;
		}
		Document firstChunkNotFinished = db.getCollection(TABLE_CHUNKS)
				.find(new Document(CHUNK_STATUS_PROP_FIELD, DbConnection.Status.START.name()))
				.sort(new Document(CHUNK_STARTED_PROP_FIELD, DbConnection.UNCOMMON)).first();
		if (firstChunkNotFinished != null) {
			cnkNum = firstChunkNotFinished.getLong(CHUNK_NUM_PROP_FIELD);
			if (finalChunk == null || cnkNum <= finalChunk) {
				Document update = new Document().append("$inc", new Document().append("workinOnCount", 1L));
				db.getCollection(TABLE_CHUNKS).findOneAndUpdate(firstChunkNotFinished, update);

				return cnkNum;
			}
		}

		return null;
	}

	private void createNewChunk(Long cnkNum) {
		
		MongoCollection<Document> collection = db.getCollection(TABLE_CHUNKS);
		Document document = new Document();
		document.put(CHUNK_NUM_PROP_FIELD, cnkNum);
		document.put(CHUNK_STATUS_PROP_FIELD, Status.START.name());
		document.put(CHUNK_STARTED_PROP_FIELD, new Date());
		document.put(CHUNK_END_PROP_FIELD, null);
		collection.insertOne(document);

	}

	@Override
	public Long getFinalChunk() {

		Document confDoc = db.getCollection(TABLE_CONFIG)
				.find(Filters.exists(CONFIG_STATUS_PROP_FIELD)).first();
		if (confDoc != null)
			return  confDoc.getLong(CONFIG_FINALCHUNK_PROP_FIELD);
		return null;
	}

	@Override
	public void setFinalChunk(Long fChunk) {
		MongoCollection<Document> collection = db.getCollection(TABLE_CONFIG);

		Document confDoc = collection.find(Filters.exists(CONFIG_STATUS_PROP_FIELD)).first();

		Document newDocument = new Document().append("$set",
				new Document().append(CONFIG_FINALCHUNK_PROP_FIELD, fChunk));

		collection.updateOne(confDoc, newDocument);

	}

	@Override
	public boolean updateChunkMap(Long chunk, Map<String, Integer> map) {
		MongoCollection<Document> collection = db.getCollection(TABLE_CHUNKS);
		Document filter = new Document().append(CHUNK_NUM_PROP_FIELD, chunk).append(CHUNK_STATUS_PROP_FIELD,
				Status.START.name());
		Document update = new Document().append("$set", new Document()
				.append(CHUNK_STATUS_PROP_FIELD, Status.DONE.name()).append(CHUNK_END_PROP_FIELD, new Date()));
		Document dChunk = collection.findOneAndUpdate(filter, update);
		if (dChunk != null)
			updateWordsMap(map);

		return dChunk == null;
	}

	private void updateWordsMap(Map<String, Integer> map) {
		FindOneAndUpdateOptions fOuo = new FindOneAndUpdateOptions().upsert(true);

		MongoCollection<Document> collection = db.getCollection(TABLE_WORDS);

		for (Entry<String, Integer> e : map.entrySet()) {

			Document filter = new Document().append(WORD_WORDNAME_PROP_FIELD, e.getKey());
			Document update = new Document().append("$inc",
					new Document().append(WORD_COUNT_PROP_FIELD, e.getValue().longValue()));
//			Document dWord = 
					collection.findOneAndUpdate(filter, update, fOuo);
			// System.out.println(dWord==null);
		}
	}

	@Override
	public Map<String, Long> findMost(Integer numberOfResult, Integer common) {

		Map<String, Long> map = new LinkedHashMap<>();
		db.getCollection(TABLE_WORDS).find()
		.sort(new Document(WORD_COUNT_PROP_FIELD, common)).limit(numberOfResult)
				.iterator().forEachRemaining(
						d -> map.put(d.getString(WORD_WORDNAME_PROP_FIELD), d.getLong(WORD_COUNT_PROP_FIELD)));

		return map;
	}

}
