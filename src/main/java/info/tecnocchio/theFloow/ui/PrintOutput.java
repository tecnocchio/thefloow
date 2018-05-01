/**
 * 
 */
package info.tecnocchio.theFloow.ui;

import info.tecnocchio.theFloow.db.DbConnection;
import info.tecnocchio.theFloow.db.DbWrapper;

/**
 * @author maurizio
 *
 */
public class PrintOutput {

	public PrintOutput(DbConnection m,Integer outputWordCount) {
		DbWrapper monWrap = new DbWrapper(m);
		System.out.println("Number of words found: "+monWrap.countWords());
		System.out.println("Less "+outputWordCount+" used words:");
		monWrap.getLessUsedWords(outputWordCount).entrySet().forEach(System.out::println);
		System.out.println("Most "+outputWordCount+" used words:");
		monWrap.getMostUsedWords(outputWordCount).entrySet().forEach(System.out::println);
		System.out.println("Most "+outputWordCount+" long words:");
		monWrap.getMostLongWords(outputWordCount).entrySet().forEach(System.out::println);
	
		
	}

}
