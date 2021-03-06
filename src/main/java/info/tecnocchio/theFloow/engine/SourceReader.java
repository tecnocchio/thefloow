package info.tecnocchio.theFloow.engine;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
/**
 * The theFloow program is an application that simply parse wikimedia dump to
 * count words.
 *
 * @author Maurizio Barbato
 * @email tecnocchio@gmail.com
 * 
 */
public class SourceReader {

	LineIterator it;
	private Long lastchunk = -1L;

	public SourceReader(String src) throws IOException {
		File file = new File(src);

		it = FileUtils.lineIterator(file, "UTF-8");
//		long lines=countLines(src);
		
	}
/*
 * It was possible to count file lines before read the chunks 
 */
	@SuppressWarnings("unused")
	private long countLines(String src) throws IOException {
		    InputStream is = new BufferedInputStream(new FileInputStream(src));
	        int count = 0;
	        boolean empty = true;

		    try {
		        byte[] c = new byte[1024];
		        int readChars = 0;
		        while ((readChars = is.read(c)) != -1) {
		            empty = false;
		            for (int i = 0; i < readChars; ++i) {
		                if (c[i] == '\n') {
		                    ++count;
		                }
		            }
		        }
		    } finally {
		        is.close();
		    }
	        return (count == 0 && !empty) ? 1 : count;
		}
	
/*
	private BufferedReader getBufferedReaderForCompressedFile(String fileIn)
			throws FileNotFoundException, CompressorException {
		FileInputStream fin = new FileInputStream(fileIn);
		BufferedInputStream bis = new BufferedInputStream(fin);
		CompressorInputStream input = new CompressorStreamFactory().createCompressorInputStream(bis);
		BufferedReader br2 = new BufferedReader(new InputStreamReader(input));
		return br2;
	}
*/


	private void setLastchunk(Long i) {
		lastchunk = i;

	}

	public void close() throws IOException {
		it.close();

	}

	public Long getLastchunk() {
		return lastchunk;
	}

	public String getNextChunk(Long pieceToWork, Integer numberOfLines) {
//		if (pieceToWork%10==0)
		System.out.println("Working on chunk number:"+pieceToWork);
		StringBuffer sb = new StringBuffer();
		for (int x = 0; x < numberOfLines; x++) {
			if (!it.hasNext()) {
				setLastchunk(pieceToWork);
				break;
			}
			String tmp = it.nextLine();
			sb.append(tmp);
			sb.append(" ");
			// System.out.println(x+" "+tmp);
		}
		return sb.toString();
	}
	/*
	 *  read the chunk skipping all line before chunk we want to read
	 *  if we found end of file we set the final chunk
	 */
	public String getNextChunkSkip(Long pieceToWork, Integer numOfLines) throws IOException {
		//if (pieceToWork%2==0)
//		System.out.println(pieceToWork+" nextChunkSkip "+System.currentTimeMillis());

		for (Long x = 0L; x < pieceToWork * numOfLines; x++) {
			if (!it.hasNext()) {
				setLastchunk(x);
				return "";
			}
			it.nextLine();
		}

		return getNextChunk(pieceToWork, numOfLines);
	}

}
