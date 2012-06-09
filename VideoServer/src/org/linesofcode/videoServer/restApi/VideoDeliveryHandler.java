package org.linesofcode.videoServer.restApi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.linesofcode.videoServer.VideoServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class VideoDeliveryHandler implements HttpHandler {

	private static final Logger LOG = Logger.getLogger(VideoDeliveryHandler.class);
	
	private VideoServer videoServer;
	
	public VideoDeliveryHandler(VideoServer videoServer) {
		this.videoServer = videoServer;
	}
	
	@Override
	public void handle(HttpExchange e) throws IOException {
		
		if(!e.getRequestMethod().toUpperCase().equals("GET")) {
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
		
		String videoId = parseVideoId(e.getRequestURI().toString());
		if(videoId == null || videoId.isEmpty()) {
			e.sendResponseHeaders(400, 0);
			e.close();
			LOG.debug("Rejected request with invalid ID");
			return;
		}
		
		InputStream in;
		
		try {
			in = videoServer.loadVideo(videoId);
		} catch(FileNotFoundException ex) {
			e.sendResponseHeaders(404, 0);
			e.close();
			LOG.error("File not found: " + ex.getMessage());
			return;
		} catch(Exception ex) {
			e.sendResponseHeaders(500, 0);
			e.close();
			LOG.error("IO Error: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}
		
		sendHeaders(e);
		sendBody(e, in);
	}

	private String parseVideoId(String uri) {
		String[] parts = uri.split("/");
		return parts[parts.length-1];
	}

	private void sendHeaders(HttpExchange e) throws IOException {
		e.getResponseHeaders().add("Content-Type", "video/mp4");
		e.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
		e.sendResponseHeaders(200, 0);
	}
	
	private void sendBody(HttpExchange e, InputStream in) throws IOException {
		OutputStream out = e.getResponseBody();
		while(true) {
			byte[] b = new byte[512];
			int read = in.read(b);
			if(read == 0) {
				break;
			}
			out.write(b);
		}
	}
}
