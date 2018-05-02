/**
* The theFloow program is an application that
* simply parse wikimedia dump to count words.
*
* @author Maurizio Barbato
* @email tecnocchio@gmail.com
* 
*/
package info.tecnocchio.theFloow;

import info.tecnocchio.theFloow.db.DatabaseAccessException;
import info.tecnocchio.theFloow.engine.ProcessController;
import info.tecnocchio.theFloow.ui.ArgumentsException;
import info.tecnocchio.theFloow.ui.ParseArguments;
import info.tecnocchio.theFloow.ui.ParsedArguments;

/**
 * Start Class
 *
 */
public class MainClass {

	/**
	 * If arguments are clear enough we start the process
	 */
	public MainClass(String[] args) {
		
		ParsedArguments a = null;
		try {
			a = ParseArguments.parse(args);
		} catch (ArgumentsException ae) {
			System.err.println(ae.getMessage());
			return;
		}
		runProcess(a);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new MainClass(args);

	}

	private void runProcess(ParsedArguments a) {
		try {
			new ProcessController(a).start();
		} catch (DatabaseAccessException e) {
			System.err.println(e.getMessage());
		}

	}

}
