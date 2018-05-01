/**
 * 
 */
package info.tecnocchio.theFloow.db;

import java.util.Map;


/**
* The theFloow program is an application that
* simply parse wikimedia dump to count words.
*
* @author Maurizio Barbato
* @email tecnocchio@gmail.com
* 
*/
/*
 * Database connection interface, we can easily have different kind of storage
 */
public interface DbConnection {
	/*
	 * If we use constant and enumeration it will be less easy to make mistakes
	 */
	
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

	/**
	 * @param host name of the host for connection
	 * @param port
	 * @param dbcName database name
	 * @param user 
	 * @param pwd
	 * @return true if connected
	 */
	public boolean connect(String host, Integer port,String dbcName,String user,String pwd) ;

	/**
	 * Close the connection.
	 */
	public void close();

	/**
	 * chek if db structure is ok, if configuration data is ready
	 */
	public void checkDbStructure();
	
	/**
	 * @return the number of final chunk if stored
	 */
	public Long getFinalChunk();
	
	/**
	 * @param cnkFinal 
	 * set on database the number of last chunk of the file
	 */
	public void setFinalChunk(Long cnkFinal);
	
	/**
	 * @param pieceToWork the number of chunk worked
	 * @param map a map with values from this piece of text
	 * if the chunk was updated from another process, do not add count to words.
	 */
	public void updateChunkMap(Long pieceToWork,Map<String, Integer> map);

	/**
	 * @param numberOfResult how many value the user want to see in output
	 * @param common ( most used, bigger value / lest used lowest value)
	 * @return a map with the query results
	 */
	public Map<String, Long> findMost(Integer numberOfResult, Integer common);
	/**
	 * @param numberOfResult how many value the user want to see in output
	 * @param common ( most long words found )
	 * @return a map with the query results
	 */
	public Map<String, Integer> findMostLong(Integer numberOfResult, Integer common);

	/**
	 * @return number of word found
	 */
	public Long countWordsInDb();

	/**
	 * @param currentChunk chunk worked
	 * @param jump number of chunk to jump
	 * @return the preferred chunk to work or null if finished
	 */
	public Long getNextChunkToWork(Long currentChunk, Long jump);
	
}
