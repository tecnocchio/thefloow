/**
 * 
 */
package info.tecnocchio.theFloow.db;

/**
 * @author maurizio
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
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public DatabaseAccessException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public DatabaseAccessException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DatabaseAccessException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated constructor stub
	}

}
