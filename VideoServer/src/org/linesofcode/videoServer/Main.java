package org.linesofcode.videoServer;

import java.io.IOException;

import org.apache.log4j.Logger;

public class Main {

	private static final Logger LOG = Logger.getLogger(Main.class);
	
	public static void main(String[] args) {
		
		final VideoServer videoServer = new VideoServer();
		evalArgs(args, videoServer);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					videoServer.shutDown();
				} catch (IOException e) {
					LOG.error("Error while shutting down server: " + e);
				}
			}
		});
		
		try {
			videoServer.start();
		} catch (IOException e) {
			LOG.error("Startup failed: " + e);
		}
	}

	private static void evalArgs(String[] args, VideoServer videoServer) {
		// TODO evaluate arguments here
	}
	
}
