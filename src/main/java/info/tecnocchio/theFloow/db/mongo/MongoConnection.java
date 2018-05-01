/**
 * 
 */
package info.tecnocchio.theFloow.db.mongo;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;

import info.tecnocchio.theFloow.db.DbConnection;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

/**
 * The theFloow program is an application that simply parse wikimedia dump to
 * count words.
 *
 * @author Maurizio Barbato
 * @email tecnocchio@gmail.com
 * 
 */
/*
 * this class is the mongodb approach to the data storing and info sharing
 */

public class MongoConnection implements DbConnection {

	private MongoClient mongo;
	private MongoDatabase db;

	public MongoConnection() {
		/*
		 * Mongodb driver puts a lot of information on output, so i raise level at
		 * warning
		 */
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		ch.qos.logback.classic.Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
		rootLogger.setLevel(Level.WARN);
	}

	@Override
	public void close() {
		mongo.close();

	}

	@Override
	public boolean connect(String mongoHost, Integer mongoPort, String name, String user, String pwd) {
		try {
			mongo = new MongoClient(mongoHost, mongoPort == null ? 27017 : mongoPort);
			// create a database wich name is extracted from filename
			db = mongo.getDatabase(name);
			// i do an operation to be sure about database correctly read
			db.listCollectionNames().iterator().forEachRemaining(a -> {
			});
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void checkDbStructure() {
		/*
		 * i look in collection config if find a document with status attribute
		 * if i dont i create it
		 */

		if (null == db.getCollection(TABLE_CONFIG)
				.find(Filters.exists(CONFIG_STATUS_PROP_FIELD))
				.first()) {
			initDbConfig();
		}

	}

	/**
	 * create a start config document clean if they exists word and chunk collection
	 */
	private void initDbConfig() {
		/*
		 * at this point concurrency is negligible
		 */
		MongoCollection<Document> collection = db.getCollection(TABLE_CONFIG);
		Document document = new Document();
		document.put(CONFIG_STATUS_PROP_FIELD, Status.START.name());
		document.put(CONFIG_FINALCHUNK_PROP_FIELD, null);
		document.put(CONFIG_CREATED_PROP_FIELD, new Date());
		collection.insertOne(document);
		db.getCollection(TABLE_WORDS).drop();
		db.getCollection(TABLE_CHUNKS).drop();
		// a word is unique in the collection
		db.getCollection(TABLE_WORDS).createIndex(Indexes.ascending(WORD_WORDNAME_PROP_FIELD),
				new IndexOptions().unique(true));
		// this is expensive but provide a fast search
		db.getCollection(TABLE_WORDS).createIndex(Indexes.ascending(WORD_COUNT_PROP_FIELD));
		// a chunk number is unique in the collection
		db.getCollection(TABLE_CHUNKS).createIndex(Indexes.ascending(CHUNK_NUM_PROP_FIELD),
				new IndexOptions().unique(true));
	}

	@Override
	public Long getNextChunkToWork(Long currentChunk, Long jump) {
		/*
		 * this function is among most important of the whole process,
		 * optimizing this job means optimizing a lot the time
		 * on my test the speed of reading the file was the bottleneck as supposed to be
		 * organize a good sequential reading from different process is the key 
		 * to the fast approach
		 * this function could have a lot of funny work inside with a lot of time improving
		 * what i did is a simple approach but i wanted to have a result 
		 */

		Long finalChunk = getFinalChunk();// check if someone else discovered final chunk

		// as a first preferred choice there is the next chunk 
		Document cnkDoc = db.getCollection(TABLE_CHUNKS).find(Filters.eq(CHUNK_NUM_PROP_FIELD, currentChunk + 1))
				.first();
		// next chunk is free to work(cnkDoc == null)  and current is not beyond final chunk
		if (cnkDoc == null && (finalChunk == null || finalChunk > (currentChunk + 1L))) {
			// create the new chunk and select it for work, no worries about concurrency, 
			// in the worst case the chunk is worked twice, but only first commit words count
			createNewChunk(currentChunk + 1);
			return currentChunk + 1;
		}
		// next chunk is not free so:
		// search for bigger chunk in progress and try to skip jump
		cnkDoc = db.getCollection(TABLE_CHUNKS).find()
				.sort(new Document(CHUNK_NUM_PROP_FIELD, DbConnection.COMMON)).first();
		// last chunk present  ( with bigger number )		
		long cnkNum = cnkDoc.getLong(CHUNK_NUM_PROP_FIELD) + jump; 
		// new chunk number proposed, can't exists because is max+jump			
		if (finalChunk == null || cnkNum <= finalChunk) {
			// the proposed is not beyond end of file
			createNewChunk(cnkNum);
			return cnkNum;
		}
		// after the jump over the last we are beyond end of file  so :
		// we have to look for holes 
        // next code is ugly, i am really sorry for this : 
		// search missing chunk from start
		// to finalChunk 
		for (Long g = 0L; g < finalChunk; g++) {
			if (db.getCollection(TABLE_CHUNKS).find(Filters.eq(CHUNK_NUM_PROP_FIELD, g)).first() == null) {
				createNewChunk(g);
				return g;
			}
		}
		// once all hole are closed, we recover the process broken, stopped, really slow... 
		// search unfinished chunk
		Document firstChunkNotFinished = db.getCollection(TABLE_CHUNKS)
				.find(new Document(CHUNK_STATUS_PROP_FIELD, DbConnection.Status.START.name()))
				.sort(new Document(CHUNK_STATUS_PROP_FIELD, DbConnection.UNCOMMON)).first();
		if (firstChunkNotFinished != null) {
			cnkNum = firstChunkNotFinished.getLong(CHUNK_NUM_PROP_FIELD);
			if (finalChunk == null || cnkNum <= finalChunk) {
				// for debug I provide an increment on chunk over which works more processes  
				Document update = new Document().append("$inc", new Document().append(CHUNK_NUMWORKING_PROP_FIELD, 1L));
				db.getCollection(TABLE_CHUNKS).findOneAndUpdate(firstChunkNotFinished, update);
				// only the fastest process will push data of the chunk
				return cnkNum;
			}
		}
		// we have succesfully finished, no other chunk to work
		return null;
	}

	private void createNewChunk(Long cnkNum) {
		try {
		MongoCollection<Document> collection = db.getCollection(TABLE_CHUNKS);
		Document document = new Document();
		document.put(CHUNK_NUM_PROP_FIELD, cnkNum);
		document.put(CHUNK_STATUS_PROP_FIELD, Status.START.name());
		document.put(CHUNK_STARTED_PROP_FIELD, new Date());
		document.put(CHUNK_END_PROP_FIELD, null);
		collection.insertOne(document);
		}
		catch(MongoWriteException me) {
			// concurrency problem
			// in the worst case a chunk is worked twice, 
			// but pushed once, so is a risk of only time waste
			// it is supposed to be really rare
		}
	}

	@Override
	public Long getFinalChunk() {
		//retrieve the value from config document of last chunk if found
		Document confDoc = db.getCollection(TABLE_CONFIG).find(Filters.exists(CONFIG_STATUS_PROP_FIELD)).first();
		if (confDoc != null)
			return confDoc.getLong(CONFIG_FINALCHUNK_PROP_FIELD);
		return null;
	}

	@Override
	public void setFinalChunk(Long fChunk) {
		
		//store the value in config document of last chunk 

		MongoCollection<Document> collection = db.getCollection(TABLE_CONFIG);

		Document confDoc = collection.find(Filters.exists(CONFIG_STATUS_PROP_FIELD)).first();

		Document newDocument = new Document().append("$set",
				new Document().append(CONFIG_FINALCHUNK_PROP_FIELD, fChunk));

		collection.updateOne(confDoc, newDocument);

	}

	@Override
	public void updateChunkMap(Long chunk, Map<String, Integer> map) {
		/*
		 * This is the second most important function of the whole program
		 * every time a chunk is worked we have to put values in mongo
		 * the sql way is to close the chunk and update the map in a single transaction
		 * mongo admit atomic operation inside a single document otherwise 
		 * MongoDB 4.0, scheduled for release in Summer 2018, 
		 * will add support for multi-document transactions.    
		 * In a simple  solution a program break during the updateWordsMap()
		 * will lost word count.
		 * a trick to solve the problem could be :
		 * I could find the words which had no chunknumber property and update only 
		 * if not updated in the single operation
		 * this will produce a number of property to words that i don't like but it work
		 * if the process is stopped in the while only non updated 
		 * words will be updated from the next process  
		 * I could  update chunk status at the end, when all words are processed
		 * 
		 */

		updateWordsMap(map,chunk);
		MongoCollection<Document> collection = db.getCollection(TABLE_CHUNKS);
		Document filter = new Document().append(CHUNK_NUM_PROP_FIELD, chunk)
				.append(CHUNK_STATUS_PROP_FIELD,Status.START.name());
		Document update = new Document().append("$set", new Document()
				.append(CHUNK_STATUS_PROP_FIELD, Status.DONE.name())
				.append(CHUNK_END_PROP_FIELD, new Date()));
		Document dChunk = collection.findOneAndUpdate(filter, update);
	

	}

	private void updateWordsMap(Map<String, Integer> map,long chunk) {
		// if we don't find the word we create it
		FindOneAndUpdateOptions fOuo = new FindOneAndUpdateOptions().upsert(true);
		
		MongoCollection<Document> collection = db.getCollection(TABLE_WORDS);
		// for each word to update 
		for (Entry<String, Integer> e : map.entrySet()) {
			// filter find the word to update
			// and is not updated for current chunk
			Document filter = new Document().append(WORD_WORDNAME_PROP_FIELD, e.getKey())
					.append("chunks.c"+chunk, new Document("$exists", false));
			// the document will increment count
			// add to the set of chunks current chunk number
			Document update = new Document().append("$inc",
					new Document().append(WORD_COUNT_PROP_FIELD, e.getValue().longValue()));
			update.append("$addToSet", new Document()
					.append("chunks",new Document("c"+chunk,"ok")));
			// process the operation
			collection.findOneAndUpdate(filter, update, fOuo);

		}
	}

	@Override
	public Map<String, Long> findMost(Integer numberOfResult, Integer common) {

		Map<String, Long> map = new LinkedHashMap<>();
		db.getCollection(TABLE_WORDS).find()
		.sort(new Document(WORD_COUNT_PROP_FIELD, common)).limit(numberOfResult)
				.iterator()
				.forEachRemaining(d -> map.put(d.getString(WORD_WORDNAME_PROP_FIELD), d.getLong(WORD_COUNT_PROP_FIELD)));

		return map;
	}

	@Override
	public Map<String, Integer> findMostLong(Integer numberOfResult, Integer common) {
		Map<String, Integer> map = new LinkedHashMap<>();

		MongoCollection<Document> collection = db.getCollection(TABLE_WORDS);

		collection
				.aggregate(
						Arrays.asList(
								new Document("$project",
										new Document("word", 1).append("field_length",
												new Document("$strLenCP", "$word"))),
								new Document("$sort", new Document("field_length", -1)),
								new Document("$project", new Document("field_length", 0)),
								new Document("$limit", numberOfResult)))
				.iterator().forEachRemaining(d -> map.put(d.getString(WORD_WORDNAME_PROP_FIELD),
						 d.getString(WORD_WORDNAME_PROP_FIELD).length()));
		return map;
	}

	@Override
	public Long countWordsInDb() {
		return db.getCollection(TABLE_WORDS).count();
	}

}
