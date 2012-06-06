package org.linesofcode.videoServer.restApi;

import java.io.IOException;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.linesofcode.videoServer.VideoServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class VideoUploadHandler implements HttpHandler {

	private static final Logger LOG = Logger.getLogger(VideoUploadHandler.class);
	
	private VideoServer videoServer;
	
	public VideoUploadHandler(VideoServer videoServer) {
		this.videoServer = videoServer;
	}

	@Override
	public void handle(HttpExchange e) throws IOException {
		
		if(!e.getRequestMethod().toUpperCase().equals("POST")) {
			e.sendResponseHeaders(405, 0);
			e.close();
			LOG.debug("Rejected request from host: " + e.getRemoteAddress() + " (unallowed method " + e.getRequestMethod() + ")");
			return;
		}
		
		try {
			processRequest(e);
		}catch(IOException ex) {
			LOG.error("I/O Error while handling HTTP Request: " + ex.getMessage());
			throw ex;
		} catch(Exception ex) {
			LOG.error("Unexpected error while handling HTTP Request: " + ex);
		} finally {
			e.close();
		}
	}

	private void processRequest(HttpExchange e) throws IOException {
		
	}

}
