package org.linesofcode.videoServer;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class Main {

	private static final Logger LOG = Logger.getLogger(Main.class);
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
		final VideoServer videoServer = context.getBean("videoServer", VideoServer.class);
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
