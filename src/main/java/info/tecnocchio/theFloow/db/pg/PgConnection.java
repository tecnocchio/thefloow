/**
 * 
 */
package info.tecnocchio.theFloow.db.pg;

import java.util.Map;

import info.tecnocchio.theFloow.db.DbConnection;

/**
 * @author maurizio
 *
 */
public class PgConnection implements DbConnection {

	@Override
	public boolean connect(String host, Integer port, String dbcName, String user, String pwd) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}




	@Override
	public Long getFinalChunk() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFinalChunk(Long cnkFinal) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Map<String, Long> findMost(Integer numberOfResult, Integer common) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Long> findMostLong(Integer numberOfResult, Integer common) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long countWordsInDb() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getNextChunkToWork(Long currentChunk, Long jump) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void checkDbStructure() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateChunkMap(Long pieceToWork, Map<String, Integer> map) {
		// TODO Auto-generated method stub
		
	}



}
