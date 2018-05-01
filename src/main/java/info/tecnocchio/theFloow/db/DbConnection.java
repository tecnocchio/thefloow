/**
 * 
 */
package info.tecnocchio.theFloow.db;

import java.util.Map;

/**
 * @author maurizio
 *
 */
public interface DbConnection {
	final String TABLE_CONFIG="config";
    final String TABLE_CHUNKS = "chunks";
    final String TABLE_WORDS = "words";

    final String CONFIG_STATUS_PROP_FIELD="status";
    final String CONFIG_FINALCHUNK_PROP_FIELD="finalChunk";
    final String CONFIG_CREATED_PROP_FIELD="createdDate";

    final String CHUNK_STATUS_PROP_FIELD="status";
    final String CHUNK_STARTED_PROP_FIELD="startedDate";    
    final String CHUNK_END_PROP_FIELD="endDate";    
    final String CHUNK_NUM_PROP_FIELD="cnknum";
    final String CHUNK_NUMWORKING_PROP_FIELD="workinOnCount";
    
    final String WORD_WORDNAME_PROP_FIELD="word";
    final String WORD_COUNT_PROP_FIELD="count";
    
    final Integer COMMON = -1;
	final Integer UNCOMMON = 1;
	
	enum Status {DONE,WORKING,START};

	public boolean connect(String host, Integer port,String dbcName,String user,String pwd) ;

	public void close();

	public void checkDbStructure(String fileName);
	
	public Long getNextChunkToWork(Long currentChunk);
	
	public Long getFinalChunk();
	
	public void setFinalChunk(Long cnkFinal);
	
	public boolean updateChunkMap(Long pieceToWork,Map<String, Integer> map);

	public Map<String, Long> findMost(Integer numberOfResult, Integer common);
	
}
