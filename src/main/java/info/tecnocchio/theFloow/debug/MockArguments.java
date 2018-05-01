/**
 * 
 */
package info.tecnocchio.theFloow.debug;

import info.tecnocchio.theFloow.ui.ParsedArguments;

/**
 * @author maurizio
 *
 */
public class MockArguments {

	public static ParsedArguments get() {
		// TODO Auto-generated method stub
		return new ParsedArguments(
		
		//		"/media/maurizio/pen/enwiki-latest-pages-articles-multistream.xml.part"
				"/media/maurizio/pen/mock.txt"
				,"localhost",27017,"mock.txt","","",true
				,false);
	}

}
