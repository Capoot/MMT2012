package org.linesofcode.videoServer;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.linesofcode.videoServer.broadcast.Broadcaster;
import org.linesofcode.videoServer.receive.Receiver;
import org.linesofcode.videoServer.webService.WebService;

public class VideoServer {
	
	private static final Logger LOG = Logger.getLogger(VideoServer.class);
	
	private Receiver receiver;
	private Broadcaster broadcaster;
	private WebService webService;
	
	public VideoServer(WebService webService) {
		this.webService = webService;
	}
	
	public void start() throws IOException {
		webService.start();
		LOG.info("Starting Server...");
	}


	public void shutDown() throws IOException {
		LOG.info("Stopping Server...");
		webService.stop();
	}

	public static Logger getLog() {
		return LOG;
	}
}
