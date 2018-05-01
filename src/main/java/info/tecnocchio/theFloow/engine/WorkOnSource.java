/**
 * 
 */
package info.tecnocchio.theFloow.engine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import info.tecnocchio.theFloow.Config;
import info.tecnocchio.theFloow.db.DbConnection;
import info.tecnocchio.theFloow.db.DbWrapper;

/**
 * @author maurizio
 *
 */
public class WorkOnSource implements Config{


	/**
	 * 
	 */

	DbConnection dbConn;
	private String source;

	public WorkOnSource(String source, DbConnection dbC)
			throws FileNotFoundException {
		this.dbConn = dbC;
		this.source = source;
	}

	public void work() {
		DbWrapper monWrap = new DbWrapper(dbConn);
		Long oldPieceToWork = -1L;
		Long pieceToWork = monWrap.getNextPieceToWork(oldPieceToWork,JUMP);

		SourceReader sou = null;
		try {
			sou = new SourceReader(source);

			String chunk = "";

			while (pieceToWork != null && pieceToWork > -1) {
				if (pieceToWork == oldPieceToWork + 1) {
					chunk = sou.getNextChunk(pieceToWork, NUMBER_OF_LINES);
				} else {
					sou.close();  // with this reader i can't restart and skip
					sou = new SourceReader(source);
					chunk = sou.getNextChunkSkip(pieceToWork, NUMBER_OF_LINES);
				}
				Map<String, Integer> chunkMap = new WordsExtractor().count(chunk);
				monWrap.storeMap(pieceToWork, chunkMap, sou.getLastchunk());
				oldPieceToWork = pieceToWork;
				pieceToWork = monWrap.getNextPieceToWork(oldPieceToWork,JUMP);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("end");
	}
}
