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
 * This class is used to perform  action over data coming and going from database.
 * In this case in not really useful, but in a more complex structure is a good place 
 * to put common data work. 
 */
public class DbWrapper {

	private DbConnection db;

	public DbWrapper(DbConnection m) {
		db=m;
	}

	public Long getNextPieceToWork(Long currentChunk,Long jump) {
		return db.getNextChunkToWork(currentChunk, jump);
	}

	public void storeMap(Long pieceToWork, Map<String, Integer> map, Long lastChunk) {
		if (lastChunk!=null && lastChunk>-1) 
			db.setFinalChunk(lastChunk);
		db.updateChunkMap(pieceToWork, map);
		
	}

	

	public Map<String,Long> getMostUsedWords(Integer numberOfResult) {
		Map<String,Long> report=db.findMost(numberOfResult,DbConnection.COMMON);		
		return report;		
	}
	
	

	public Map<String,Long> getLessUsedWords(Integer numberOfResult) {
		return db.findMost(numberOfResult,DbConnection.UNCOMMON);	
	}

	public Map<String, Long> getMostLongWords(Integer numberOfResult) {
		return db.findMostLong(numberOfResult,DbConnection.UNCOMMON);
	}

	public Long countWords() {

		return db.countWordsInDb();
	}
}
