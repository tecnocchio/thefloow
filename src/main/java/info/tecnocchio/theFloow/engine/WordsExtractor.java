/**
 * 
 */
package info.tecnocchio.theFloow.engine;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The theFloow program is an application that simply parse wikimedia dump to
 * count words.
 *
 * @author Maurizio Barbato
 * @email tecnocchio@gmail.com
 * 
 */
public class WordsExtractor {

	private final String REGEX="[^a-zA-Z]+";
	@SuppressWarnings("unused")
	private final String REGEX_IT="[^a-zA-Z_àéèìòù]+";

	/*
	 * Uses regex to extract words
	 * create a list
	 * use Collection power to extract the map of words and its occurrence
	 * filtering zero length or null 
	 */
	public Map<String, Integer> count(String nextChunk) {

		List<String> words = Arrays.asList(nextChunk.split(REGEX));

		return words.parallelStream()
				.map((item) -> item.toLowerCase()).filter(s -> s != null && s.length() > 0)
				.collect(Collectors.toConcurrentMap(w -> w, w -> 1, Integer::sum));

	}

}
