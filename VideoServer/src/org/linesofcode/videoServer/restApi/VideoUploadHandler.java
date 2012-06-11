package org.linesofcode.videoServer.restApi;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.log4j.Logger;
import org.linesofcode.videoServer.VideoServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class VideoUploadHandler implements HttpHandler {

	private static final Logger LOG = Logger.getLogger(VideoUploadHandler.class);
	
	private VideoServer videoServer;
	private String tempDir;
	
	public VideoUploadHandler(VideoServer videoServer) {
		this.videoServer = videoServer;
	}

	@Override
	public void handle(HttpExchange e) throws IOException {
		
		if(!e.getRequestMethod().toUpperCase().equals("POST")) {
			respondError(e, 405);
			e.close();
			LOG.debug("Rejected request from host: " + e.getRemoteAddress() + " (unallowed method " + e.getRequestMethod() + ")");
			return;
		}
		
		try {
			processRequest(e);
			respondSuccessful(e);
		}catch(IOException ex) {
			LOG.error("I/O Error while handling HTTP Request: " + ex.getMessage());
			throw ex;
		} catch(NumberFormatException ex) {
			LOG.error("Error Reading number field: " + ex.getMessage());
			respondError(e, 400, ex);
		} catch(Exception ex) {
			LOG.error("Unexpected error while handling HTTP Request: " + ex);
			respondError(e, 500, ex);
		} finally {
			e.close();
		}
	}

	private void processRequest(HttpExchange e) throws IOException, FileUploadException {
		FileUpload upload = new FileUpload(new LightweightFileItemFactory(tempDir));
		@SuppressWarnings("rawtypes")
		List items = upload.parseRequest(new LightweightHttpRequestContext(e));
		
		String id = null;
		double lat = 0.0;
		double lng = 0.0;
		InputStream data = null;
		String title = null;
		
		for(Object o : items) {
			FileItem item = (FileItem)o;
			if(item.getFieldName().toLowerCase().equals("video")) {
				data = item.getInputStream();
				id = makeId(item.getName());
				continue;
			}
			if(item.getFieldName().toLowerCase().equals("lat")) {
				lat = Double.parseDouble(item.getString());
				continue;
			}
			if(item.getFieldName().toLowerCase().equals("lng")) {
				lng = Double.parseDouble(item.getString());
				continue;
			}
			if(item.getFieldName().toLowerCase().equals("title")) {
				title = item.getString();
			}
		}
		
		// TODO the following operations could be threaded
		// TODO we could send positive response before processing
		try {
			videoServer.saveVideo(data, id, lat, lng, title);
		} finally {
			LOG.debug("Deleting temporary files...");
			for(Object o : items) {
				FileItem item = (FileItem)o;
				item.delete();
			}
		}
	}

	private String makeId(String name) {
		Random r = new Random();
		r.setSeed(System.currentTimeMillis());
		String cleanName = name.toLowerCase().replace(" ", "").replace(".", "");
		return String.format("%s%d", cleanName, Math.abs(r.nextInt()));
	}
	
	private void respondSuccessful(HttpExchange e) throws IOException {
		e.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
		e.sendResponseHeaders(200, 0);
	}
	
	private void respondError(HttpExchange e, int code) throws IOException {
		e.sendResponseHeaders(code, 0);
	}
	
	private void respondError(HttpExchange e, int code, Exception cause) throws IOException {
		respondError(e, code);
		PrintWriter out = new PrintWriter(e.getResponseBody(), true);
		out.println(cause.getMessage());
		// TODO stack trace printing only if configured
		cause.printStackTrace();
	}

	public void setTempDir(String uploadTempDir) {
		tempDir = uploadTempDir;
	}
}
