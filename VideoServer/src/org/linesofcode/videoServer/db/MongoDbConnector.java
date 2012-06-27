package org.linesofcode.videoServer.db;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoDbConnector {

	private static final Logger LOG = Logger.getLogger(MongoDbConnector.class);
	
	private String hostName;
	private Integer port = null;
	private String dbname;
	
	private Mongo mongo;
	
	public void start() throws MongoException, IOException {
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
	}
	
	public DB getDb() {
		LOG.debug("Retrieving data base " + dbname);
		return mongo.getDB(dbname);
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
}
