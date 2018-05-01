/**
 * 
 */
package info.tecnocchio.theFloow.ui;

/**
 * @author maurizio
 *
 */
public class ParsedArguments {

	private String hostDb;
	private Integer portDb;
	private String nameDb;
	private String userDb;
	private String pwdDb;
	private boolean useMongoDb;
	private Integer outputCount;
	private boolean output;
	private String fileName;

	/**
	 * 
	 */
	public ParsedArguments() {
		// TODO Auto-generated constructor stub
	}

	public boolean isOutput() {
		return output;
	}

	public void setOutput(boolean output) {
		this.output = output;
	}

	/**
	 * @param hostDb
	 * @param portDb
	 * @param nameDb
	 * @param userDb
	 * @param pwdDb
	 * @param useMongoDb
	 * @param output
	 */
	public ParsedArguments(String filename, String hostDb, Integer portDb, String nameDb, String userDb, String pwdDb,
			boolean useMongoDb, boolean output) {
		super();
		this.hostDb = hostDb;
		this.portDb = portDb;
		this.nameDb = nameDb;
		this.userDb = userDb;
		this.pwdDb = pwdDb;
		this.useMongoDb = useMongoDb;
		this.output = output;
		this.fileName = filename;
	}

	public String getHostDb() {
		return hostDb;
	}

	public void setHostDb(String hostDb) {
		this.hostDb = hostDb;
		this.setUseMongoDb(true);
	}

	public Integer getPortDb() {
		return portDb;
	}

	public void setPortDb(Integer portDb) {
		this.portDb = portDb;
	}

	public String getNameDb() {
		return nameDb;
	}

	public void setNameDb(String nameDb) {
		this.nameDb = nameDb;
	}

	public String getUserDb() {
		return userDb;
	}

	public void setUserDb(String userDb) {
		this.userDb = userDb;
	}

	public String getPwdDb() {
		return pwdDb;
	}

	public void setPwdDb(String pwdDb) {
		this.pwdDb = pwdDb;
	}

	public boolean isUseMongoDb() {
		return useMongoDb;
	}

	public void setUseMongoDb(boolean useMongoDb) {
		this.useMongoDb = useMongoDb;
	}

	public boolean isOnMongo() {
		return useMongoDb;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setOutputCount(Integer wordDisplayed) {
		this.outputCount=wordDisplayed;
		if (wordDisplayed>0)
		this.output=true;
	}
	public Integer getOutputCount() {
		return outputCount;
	}


}
