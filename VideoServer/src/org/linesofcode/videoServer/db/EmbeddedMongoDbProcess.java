package org.linesofcode.videoServer.db;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class EmbeddedMongoDbProcess {

	private static final Logger LOG = Logger.getLogger(EmbeddedMongoDbProcess.class);
	
	private String command;
	private String dataPath;
	private String hostName;
	private Integer port = null;
	private String dbname;
	private String mongoPath;
	
	private Mongo mongo;
//	private Process process;
	
	public void start() throws MongoException, IOException {

//		LOG.debug("Spawning MongoDB process...");
//		ProcessBuilder pb = new ProcessBuilder(command, String.format("--dbpath %s", dataPath), String.format("--port %d", port));
//		pb.directory(new File(mongoPath));
//		process = pb.start();
		
		if(port == null) {
			LOG.debug("Connecting to Database " + hostName + "...");
			mongo = new Mongo(hostName);
		} else {
			LOG.debug("Connecting to Database " + hostName + ":" + port + "...");
			mongo = new Mongo(hostName, port);
		}
	}
	
	public void stop() {
		LOG.debug("Disconnecting from data base...");
		mongo.close();
//		LOG.debug("Terminating MongoDB process...");
//		process.destroy();
	}
	
	public DB getDb() {
		LOG.debug("Retrieving data base " + dbname);
		return mongo.getDB(dbname);
	}
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public String getMongoPath() {
		return mongoPath;
	}

	public void setMongoPath(String mongoPath) {
		this.mongoPath = mongoPath;
	}
}
