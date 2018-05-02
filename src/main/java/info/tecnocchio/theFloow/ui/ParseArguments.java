/**
 * 
 */
package info.tecnocchio.theFloow.ui;

import java.io.File;

//import info.tecnocchio.theFloow.debug.MockArguments;

/**
 * The theFloow program is an application that simply parse wikimedia dump to
 * count words.
 *
 * @author Maurizio Barbato
 * @email tecnocchio@gmail.com
 * 
 */
public class ParseArguments {

	/**
	 * @param args
	 * try to do all a series of check before start
	 * take arguments only if are fine
	 */

	public static ParsedArguments parse(String[] args) throws ArgumentsException {

		// –source dump.xml –mongo [hostname]:[port] -output 7
		if (args == null || args.length < 3)
			throw new ArgumentsException(
					"Source file and mongo server is mandatory,params example:  –source dump.xml –mongo hostname[:port] [-output numberOfWordMostAndLessUsedToShow]");
		ParsedArguments pa = new ParsedArguments();
		if ("-source".equals(args[0].toLowerCase()))
			pa.setFileName(args[1]);
		if (args.length > 3 && "-source".equals(args[2].toLowerCase()))
			pa.setFileName(args[3]);
		if (args.length > 5 && "-source".equals(args[4].toLowerCase()))
			pa.setFileName(args[5]);

		if ("-mongo".equals(args[0].toLowerCase()))
			setMongo(pa, args[1]);
		if (args.length > 3 && "-mongo".equals(args[2].toLowerCase()))
			setMongo(pa, args[3]);
		if (args.length > 5 && "-mongo".equals(args[4].toLowerCase()))
			setMongo(pa, args[5]);

		if ("-output".equals(args[0].toLowerCase()))
			pa.setOutputCount(getNum(args[1]));
		if (args.length > 3 && "-output".equals(args[2].toLowerCase()))
			pa.setOutputCount(getNum(args[3]));
		if (args.length > 5 && "-output".equals(args[4].toLowerCase()))
			pa.setOutputCount(getNum(args[5]));

		if (!pa.isOnMongo())
			throw new ArgumentsException("no mongo server specified!");
		if (null == pa.getFileName())
			throw new ArgumentsException("no source file specified!");
		File f= new File(pa.getFileName());
		if (!f.exists() )
			throw new ArgumentsException("can't find source file specified!");
		if (!f.isFile() )
			throw new ArgumentsException("problem with source file specified!");
		
			
		pa.setNameDb(extractFromFileName(pa.getFileName()));
		return pa;
	}

	private static String extractFromFileName(String fileName) {
		if (fileName.contains("/"))
			fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
		if (fileName.contains("\\"))
			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
		fileName = fileName.replaceAll("[.]", "_");
		return fileName;
	}

	private static Integer getNum(String param) throws ArgumentsException {
		try {
			return (Integer.parseInt(param));
		} catch (Exception e) {
			throw new ArgumentsException("output number of word attribute wrong!");
		}
	}

	private static void setMongo(ParsedArguments pa, String param) throws ArgumentsException {
		String[] params = param.split(":");
		pa.setHostDb(params[0]);
		pa.setPortDb(getMongoPort(params));
	}

	private static Integer getMongoPort(String[] params) throws ArgumentsException {
		if (params.length > 1) {
			try {
				return (Integer.parseInt(params[1]));
			} catch (Exception e) {
				throw new ArgumentsException("mongo port attribute wrong!");
			}
		}
		return 27017;
	}

}
