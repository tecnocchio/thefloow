/**
 * 
 */
package info.tecnocchio.theFloow;

import info.tecnocchio.theFloow.db.DatabaseAccessException;
import info.tecnocchio.theFloow.engine.ProcessController;
import info.tecnocchio.theFloow.ui.ArgumentsException;
import info.tecnocchio.theFloow.ui.ParseArguments;
import info.tecnocchio.theFloow.ui.ParsedArguments;

/**
 * @author maurizio
 *
 */
public class MainClass {

	/**
	 * 
	 */
	public MainClass(String[] args) {
		ParsedArguments a = null;
		try {
			a = ParseArguments.parse(args);
		} catch (ArgumentsException ae) {
			System.err.println(ae.getMessage());
			return;
		}
		runInstance(a);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new MainClass(args);

	}

	private void runInstance(ParsedArguments a) {
		try {
			new ProcessController(a).start();
		} catch (DatabaseAccessException e) {
			System.err.println(e.getMessage());
		}

	}

}
