/**
 * 
 */
package info.tecnocchio.theFloow.db;

import java.util.Map;

/**
 * @author maurizio
 *
 */
public class DbWrapper {

	private DbConnection db;
	
	

	/**
	 * 
	 */
	public DbWrapper() {
		// TODO Auto-generated constructor stub
	}

	public DbWrapper(DbConnection m) {
		db=m;
	}

	public Long getNextPieceToWork(Long currentChunk) {
		 
		return db.getNextChunkToWork(currentChunk);
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
}
