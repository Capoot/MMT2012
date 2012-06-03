package org.linesofcode.videoServer.restApi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class VideoDeliveryHandler implements HttpHandler {

	private static final Logger LOG = Logger.getLogger(VideoDeliveryHandler.class);
	
	private String videoPath;
	
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
		
		String path = String.format("%s/%s.mp4", videoPath, videoId);
		File file = new File(path);
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
		} catch(FileNotFoundException ex) {
			e.sendResponseHeaders(404, 0);
			e.close();
			LOG.debug("File not found: " + path);
			return;
		}
		BufferedInputStream in = new BufferedInputStream(fis);
		
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
	
	private void sendBody(HttpExchange e, BufferedInputStream in) throws IOException {
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

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}
}
