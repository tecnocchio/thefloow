/**
 * 
 */
package info.tecnocchio.theFloow.ui;

import org.junit.Test;

/**
 * @author maurizio
 *
 */
public class ParseArgumentsTest {


	@Test(expected = ArgumentsException.class)
	public void testParseException() throws ArgumentsException {
		ParseArguments.parse(new String[]{"-source"});
	    
	}
}
