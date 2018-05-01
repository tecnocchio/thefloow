/**
 * 
 */
package info.tecnocchio.theFloow;

/**
* The theFloow program is an application that
* simply parse wikimedia dump to count words.
*
* @author Maurizio Barbato
* @email tecnocchio@gmail.com
* 
*/
public interface Config {
	/*
	 * Number of lines read for each work chunk, chunk are used to split work on multiple machines
	 * the more this is bigger less often mongo is called and more memory is required
	 * with a config of 500000 with first 2.5Gbpart of wikimedia it remain under 3Gb ram usage.    
	 */
	final Integer NUMBER_OF_LINES = 1;
	/*
	 * Number of Chunk to jump if next chunk is being worked from another application.
	 * If the piece to work follow last piece, is possible to do continue reading without jumping in the file
	 * so its preferred cause its faster.
	 * the most process are used the best is this number large.
	 * If we would be in the best case we could count the number of chunks, and then choose to split the largest 
	 * place where there are no chunks when assigning a new one, 
	 * so that each process would have the most sequential reading.   
	 */
	final Long JUMP = 5L;

}
