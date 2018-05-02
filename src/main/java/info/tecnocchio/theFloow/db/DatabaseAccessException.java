/**
 * 
 */
package info.tecnocchio.theFloow.db;

/**
* The theFloow program is an application that
* simply parse wikimedia dump to count words.
*
* @author Maurizio Barbato
* @email tecnocchio@gmail.com
* 
*/
public class DatabaseAccessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7221274458696189038L;

	/**
	 * 
	 */
	public DatabaseAccessException() {
	}

	/**
	 * @param message
	 */
	public DatabaseAccessException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DatabaseAccessException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DatabaseAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public DatabaseAccessException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
