package org.linesofcode.videoServer;

import org.apache.log4j.Logger;
import org.linesofcode.videoServer.broadcast.Broadcaster;
import org.linesofcode.videoServer.receive.Receiver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class VideoServer {
	
	private static final Logger LOG = Logger.getLogger(VideoServer.class);
	
	private final Properties properties = new Properties();
	
	private Receiver receiver;
	private Broadcaster broadcaster;
	
	public VideoServer(Receiver receiver, Broadcaster broadcaster) {
		this.receiver = receiver;
		this.broadcaster = broadcaster;
	}
	
	public void start() throws IOException {
		readProperties();
		receiver.initialize();
		broadcaster.initialize();
		LOG.info("Server launched successfully");
	}

	private void readProperties() throws IOException {
		
		final Class<?> c = VideoServer.class;
		final String path = "server.properties";
		final InputStream in = c.getClassLoader().getResourceAsStream(path);
		
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
