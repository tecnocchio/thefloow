/**
 * 
 */
package info.tecnocchio.theFloow.debug;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author maurizio
 *
 */
public class Debug {

	public static void print(Map<String, Long> map) {
		//System.out.println(map.entrySet());
		   List<Entry<String, Long>> list = new ArrayList<>(map.entrySet());
	        list.sort(Entry.comparingByValue());
	        for (int x=0;x<10;x++) {
	        	System.out.println(list.get(list.size()-x-1));
	        }
	}

}
