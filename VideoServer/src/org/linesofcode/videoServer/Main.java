package org.linesofcode.videoServer;

import org.apache.log4j.Logger;

import java.io.IOException;

public class Main {

	private static final Logger LOG = Logger.getLogger(Main.class);
	
	public static void main(String[] args) {
		
		final VideoServer videoServer = new VideoServer();
		evalArgs(args, videoServer);
        addShutdownHook(videoServer);

        try {
			videoServer.start();
		} catch (IOException e) {
			LOG.error("Startup failed.", e);
		}
	}

    private static void addShutdownHook(final VideoServer videoServer) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    videoServer.shutDown();
                } catch (IOException e) {
                    LOG.error("Error while shutting down server.",  e);
                }
            }
        });
    }

    private static void evalArgs(String[] args, VideoServer videoServer) {
		// TODO evaluate arguments here
	}
	
}
