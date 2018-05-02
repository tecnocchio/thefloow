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
 * The theFloow program is an application that simply parse wikimedia dump to
 * count words.
 *
 * @author Maurizio Barbato
 * @email tecnocchio@gmail.com
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
/*
 * Heart of the program
 * 
 */
	public void work() throws IOException {
		DbWrapper monWrap = new DbWrapper(dbConn);
		Long oldPieceToWork = -1L;
		// take the first piece to work
		Long pieceToWork = monWrap.getNextPieceToWork(oldPieceToWork,JUMP);

		SourceReader sou = new SourceReader(source);

			String chunk = "";

			while (pieceToWork != null && pieceToWork > -1) {
				// if the piece we are working is next that before we continue reading
				if (pieceToWork == oldPieceToWork + 1) {
					chunk = sou.getNextChunk(pieceToWork, NUMBER_OF_LINES);
				} else {
					// else
					sou.close();  // with this reader i can't restart and skip
					sou = new SourceReader(source);
					chunk = sou.getNextChunkSkip(pieceToWork, NUMBER_OF_LINES);
				}
				// now i get the map of words from the piece o string
				Map<String, Integer> chunkMap = new WordsExtractor().count(chunk);
				// sync this map on mongo and update the final chunk if found
				monWrap.storeMap(pieceToWork, chunkMap, sou.getLastchunk());				
				oldPieceToWork = pieceToWork;
				// search for next piece to work
				pieceToWork = monWrap.getNextPieceToWork(oldPieceToWork,JUMP);
			}
		System.out.println("end");
	}
}
