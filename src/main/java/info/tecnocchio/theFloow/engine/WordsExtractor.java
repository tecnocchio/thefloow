/**
 * 
 */
package info.tecnocchio.theFloow.engine;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author maurizio
 *
 */
public class WordsExtractor {

	private final String REGEX="[^a-zA-Z_]+";
	@SuppressWarnings("unused")
	private final String REGEX_IT="[^a-zA-Z_àéèìòù]+";

	public Map<String, Integer> count(String nextChunk) {

		List<String> words = Arrays.asList(nextChunk.split(REGEX));

		return words.parallelStream()
				.map((item) -> item.toLowerCase()).filter(s -> s != null && s.length() > 0)
				.collect(Collectors.toConcurrentMap(w -> w, w -> 1, Integer::sum));

	}

}
