package org.linesofcode.videoServer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class VideoServer {
	
	private static final Logger LOG = Logger.getLogger(VideoServer.class);
	
	private Properties properties = new Properties();
	
	private Receiver receiver;
	private Broadcaster broadcaster;
	
	public void start() throws IOException {
		readProperties();
		broadcaster = new HttpBroadcaster(
				Integer.parseInt(properties.getProperty("httpPort")),
				properties.getProperty("httpContextRoot"));
//		receiver.initialize();
		broadcaster.initialize();
		LOG.info("Server launched successfully");
	}

	private void readProperties() throws IOException {
		
		Class<?> c = VideoServer.class;
		final String path = "server.properties";
		InputStream in = c.getClassLoader().getResourceAsStream(path);
		
		if(in == null) {
			throw new FileNotFoundException(path);
		}
		
		try {
			properties.load(in);
		} finally {
			in.close();
		}
		
		LOG.debug("receiverPort: " + properties.getProperty("receiverPort"));
	}

	public void shutDown() throws IOException {
//		receiver.cleanup();
		broadcaster.cleanup();
		LOG.info("Server stopped");
	}
}
