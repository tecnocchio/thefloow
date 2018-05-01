/**
 * 
 */
package info.tecnocchio.theFloow.engine;

import java.io.FileNotFoundException;


import info.tecnocchio.theFloow.db.DatabaseAccessException;
import info.tecnocchio.theFloow.db.DbConnection;
import info.tecnocchio.theFloow.db.mongo.MongoConnection;
import info.tecnocchio.theFloow.db.pg.PgConnection;
import info.tecnocchio.theFloow.ui.ParsedArguments;
import info.tecnocchio.theFloow.ui.PrintOutput;

/**
 * @author maurizio
 *
 */
public class ProcessController {

	private ParsedArguments arg;
	private DbConnection dbConn;

	public ProcessController(ParsedArguments a) throws DatabaseAccessException {
		this.arg = a;
		if (a.isOnMongo())
			dbConn = new MongoConnection();
		else
			dbConn = new PgConnection();
		if (!dbConn.connect(a.getHostDb(), a.getPortDb(), a.getNameDb(), a.getUserDb(), a.getPwdDb()))
			throw new DatabaseAccessException("Problem with database access!");
		dbConn.checkDbStructure(a.getFileName());

	}

	public void start() {
			try {
				new WorkOnSource(arg.getFileName(), dbConn).work();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		if (arg.isOutput())	
		new PrintOutput(dbConn,arg.getOutputCount());
		dbConn.close();
	}

}
