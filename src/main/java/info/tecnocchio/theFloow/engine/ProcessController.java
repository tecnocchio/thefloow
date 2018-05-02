/**
 * 
 */
package info.tecnocchio.theFloow.engine;

import java.io.IOException;

import info.tecnocchio.theFloow.db.DatabaseAccessException;
import info.tecnocchio.theFloow.db.DbConnection;
import info.tecnocchio.theFloow.db.mongo.MongoConnection;
import info.tecnocchio.theFloow.db.pg.PgConnection;
import info.tecnocchio.theFloow.ui.ParsedArguments;
import info.tecnocchio.theFloow.ui.PrintOutput;

/**
 * The theFloow program is an application that simply parse wikimedia dump to
 * count words.
 *
 * @author Maurizio Barbato
 * @email tecnocchio@gmail.com
 * 
 */
public class ProcessController {

	private ParsedArguments arg;
	private DbConnection dbConn;

	public ProcessController(ParsedArguments a) throws DatabaseAccessException {
		
		this.arg = a;
		
		if (a.isOnMongo())
			dbConn = new MongoConnection(); // at moment only mongo supported
		else
			dbConn = new PgConnection();
		// connecting to db...
		if (!dbConn.connect(a.getHostDb(), a.getPortDb(), a.getNameDb(), a.getUserDb(), a.getPwdDb()))
			throw new DatabaseAccessException("Problem connecting to database!");
		// check db structure and init
		dbConn.checkDbStructure();

	}

	// start another process
	public void start() {
		try {
			// process the file
			new WorkOnSource(arg.getFileName(), dbConn).work();
			
		} catch (IOException e) {
			// argument processing did check file exists before
			e.printStackTrace();
			System.out.println("Error during file reading");
			return;
		}
		
		// if request give output
		if (arg.isOutput())
			new PrintOutput(dbConn, arg.getOutputCount());
		
		// close db connection
		dbConn.close();
	}

}
