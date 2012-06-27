package org.linesofcode.videoServer;

import org.apache.log4j.Logger;
import org.linesofcode.videoServer.db.BroadcastDao;
import org.linesofcode.videoServer.db.MongoDbConnector;
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
        final MongoDbConnector mongo = context.getBean("mongodb", MongoDbConnector.class);
        addShutdownHook(videoServer, restApi, mongo);
        
        LOG.info("Starting embedded MongoDB...");
        try {
        	mongo.start();
        } catch(Exception e) {
        	LOG.error("Starting embedded MongoDB failed.", e);
        	return;
        }
        
        videoServer.setBroadcastDao(context.getBean("broadcastDao", BroadcastDao.class));

        LOG.info("Starting video server...");
        try {
			videoServer.start();
		} catch (Exception e) {
			LOG.error("Video server startup failed.", e);
			return;
		}
        
        LOG.info("Starting REST API...");
        try {
			restApi.start();
		} catch (IOException e) {
			LOG.error("REST API startup failed.", e);
		}
	}

    private static void addShutdownHook(final VideoServer videoServer, final RestAPI restApi, final MongoDbConnector mongo) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    videoServer.shutDown();
                } catch (Exception e) {
                    LOG.error("Error while shutting down video server.",  e);
                }
                try {
                    restApi.stop();
                } catch (Exception e) {
                    LOG.error("Error while shutting down REST API.",  e);
                }
                try {
                	mongo.stop();
                } catch(Exception e) {
                	LOG.error("Error while shutting down embedded MongoDB",  e);
                }
                LOG.info("Shutdown hook completed");
            }
        });
    }
}
