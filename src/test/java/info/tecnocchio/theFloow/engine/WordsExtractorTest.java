/**
 * 
 */
package info.tecnocchio.theFloow.engine;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

/**
 * @author mbarbato
 *
 */
public class WordsExtractorTest {

	/**
	 * Test method for {@link info.tecnocchio.theFloow.engine.WordsExtractor#count(java.lang.String)}.
	 */
	@Test
	public void testCount() {
		WordsExtractor we= new WordsExtractor();
		Map<String, Integer> map=we.count("Hi all!  is this  a \"trick\" ?No, have a nice day! <3 ");
		
		assertNotNull(map);
		assertTrue(map.containsKey("hi"));
		assertTrue(map.size()==10);
		assertEquals(map.get("a").intValue(),2);
		
		
	}

}
