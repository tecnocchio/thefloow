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
		System.out.println(monWrap.getLessUsedWords(outputWordCount));
		System.out.println(monWrap.getMostUsedWords(outputWordCount));
		
	}

}
