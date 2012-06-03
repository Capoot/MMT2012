package org.linesofcode.videoServer;

import org.apache.log4j.Logger;
import org.linesofcode.videoServer.restApi.RestAPI;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class Main {

	private static final Logger LOG = Logger.getLogger(Main.class);
	
	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
		final VideoServer videoServer = context.getBean("videoServer", VideoServer.class);
        final RestAPI restApi = context.getBean("restApi", RestAPI.class);
        evalArgs(args, videoServer);
        addShutdownHook(videoServer,restApi);

        LOG.info("Starting video server...");
        try {
			videoServer.start();
		} catch (IOException e) {
			LOG.error("Video server startup failed.", e);
		}
        
        LOG.info("Starting REST API...");
        try {
			restApi.start();
		} catch (IOException e) {
			LOG.error("REST API startup failed.", e);
		}
	}

    private static void addShutdownHook(final VideoServer videoServer, final RestAPI restApi) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    videoServer.shutDown();
                } catch (IOException e) {
                    LOG.error("Error while shutting down video server.",  e);
                }
                try {
                    restApi.stop();
                } catch (Exception e) {
                    LOG.error("Error while shutting down REST API.",  e);
                }
            }
        });
    }

    private static void evalArgs(String[] args, VideoServer videoServer) {
		// TODO evaluate arguments here
	}
	
}
