package org.linesofcode.videoServer.restApi;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.linesofcode.videoServer.Broadcast;
import org.linesofcode.videoServer.VideoServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class VideoListHandler implements HttpHandler {

	private static Logger LOG = Logger.getLogger(VideoListHandler.class);
	
	private String responseEncoding;
	private Object hostPort;

	private VideoServer videoServer;
	
	public VideoListHandler(VideoServer videoServer) {
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
			ex.printStackTrace();
		} finally {
			e.close();
		}
	}
	
	private void processRequest(HttpExchange e) throws IOException {
		sendHeaders(e);
		sendBody(e);
	}

	private void sendHeaders(HttpExchange e) throws IOException {
		e.getResponseHeaders().add("Content-Type", "application/json");
		e.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
		e.getResponseHeaders().add("Content-Encoding", responseEncoding);
		e.sendResponseHeaders(200, 0);
	}

	private void sendBody(HttpExchange e) throws IOException {
		
		OutputStreamWriter writer = new OutputStreamWriter(e.getResponseBody(), responseEncoding);
		PrintWriter out = new PrintWriter(writer, true);
		Collection<Broadcast> casts = videoServer.getCasts();
		Iterator<Broadcast> it = casts.iterator();
		
		out.print("{");
		while(it.hasNext()) {
			out.print(castToJson(it.next()));
			if(it.hasNext()) {
				out.print(",");
			}
		}
		out.print("}");
		out.flush();
		
		e.getResponseBody().close();
	}
	
	private String castToJson(Broadcast cast) {
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("\"%s\":{", cast.getId()));
		builder.append(String.format("\"lat\":%s,", cast.getLattitude()));
		builder.append(String.format("\"lng\":%s,", cast.getLongitude()));
		builder.append(String.format("\"urls\":{%s},", makeUrls(cast)));
		builder.append(String.format("\"title\":\"%s\"", cast.getTitle()));
		builder.append("}");
		return builder.toString();
	}
	
	private String makeUrls(Broadcast cast) {
		
		String[] extensions = new String[] { "mp4", "ogv", "webm" };
		StringBuilder buffer = new StringBuilder();
		
		for(int i=0; i<extensions.length; i++) {
			buffer.append(String.format("\"video/%s\"", extensions[i]));
			buffer.append(":");
			buffer.append(String.format("\"http://%s/watch/%s.%s\"", hostPort, cast.getId(), extensions[i]));
			if(i < extensions.length -1) {
				buffer.append(",");
			}
		}
		
		return buffer.toString();
	}
	
	public String getResponseEncoding() {
		return responseEncoding;
	}

	public void setResponseEncoding(String responseEncoding) {
		this.responseEncoding = responseEncoding;
	}

	public Object getHostPort() {
		return hostPort;
	}

	public void setHostPort(Object hostPort) {
		this.hostPort = hostPort;
	}
}
