/**
 * 
 */
package info.tecnocchio.theFloow.db.pg;

import java.util.Map;

import info.tecnocchio.theFloow.db.DbConnection;

/**
* The theFloow program is an application that
* simply parse wikimedia dump to count words.
*
* @author Maurizio Barbato
* @email tecnocchio@gmail.com
* 
*/
public class PgConnection implements DbConnection {

	@Override
	public boolean connect(String host, Integer port, String dbcName, String user, String pwd) {
		// open the connection
		// this. dbcname=dbcname, for config table
		return false;
	}

	@Override
	public void close() {
		//  closes the connection

	}




	@Override
	public Long getFinalChunk() {
		// select finalchunk from config
		return null;
	}

	@Override
	public void setFinalChunk(Long cnkFinal) {
		// update config set finalchunk=cnkFinal
		
	}


	@Override
	public Map<String, Long> findMost(Integer numberOfResult, Integer common) {
		// order=comon==common?desc:asc
		// select word,count from words ordered by count order 
		return null;
	}

	@Override
	public Map<String, Integer> findMostLong(Integer numberOfResult, Integer common) {
		// order=comon==common?desc:asc
		// select word,count from words ordered by length(word) order 
		return null;
	}

	@Override
	public Long countWordsInDb() {
		// select count(*) from words
		return null;
	}

	@Override
	public Long getNextChunkToWork(Long currentChunk, Long jump) {
		// lot of stuff
		return null;
	}

	@Override
	public void checkDbStructure() {
		// check db structure or create it
		// create config row with dbcname
	}

	@Override
	public void updateChunkMap(Long pieceToWork, Map<String, Integer> map) {
		
		// begin transaction
		// update all words and chunk
		// end transaction
		
	}



}
