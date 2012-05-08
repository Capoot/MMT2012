package org.linesofcode.videoServer.webService;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.linesofcode.videoServer.Broadcast;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class HttpWebService implements WebService, HttpHandler {
	
	private static final Logger LOG = Logger.getLogger(HttpWebService.class);
	
	private HttpServer server;
	private InetSocketAddress address;
	private String httpContextName;
	private HttpContext context;
	private int port;
	
	private Map<String, Broadcast> casts = new HashMap<String, Broadcast>();

	@Override
	public void addCast(Broadcast cast) {
		casts.put(cast.getId(), cast);
	}

	@Override
	public void removeCast(Broadcast cast) {
		casts.remove(cast.getId());
	}

	@Override
	public void start() throws IOException {
		address = new InetSocketAddress(port);
		server = HttpServer.create(address, 0);
		context = server.createContext(httpContextName, this);
		server.setExecutor(Executors.newCachedThreadPool());
		
		LOG.info("Launching Http Web Service at port " + address.getPort() + ", context root: " + context.getPath() + "...");
	    server.start();
	    
	    // FIXME this is testdata
	    addCast(new Broadcast("dummy", 52, 13));
	}

	@Override
	public void stop() {
		server.stop(0);
	}

	@Override
	public void handle(HttpExchange e) throws IOException {
		
		if(!e.getRequestMethod().toUpperCase().equals("GET")) {
			e.sendResponseHeaders(405, 0);
			e.close();
			LOG.debug("Rejected request from host: " + e.getRemoteAddress() + " (unallowed method " + e.getRequestMethod() + ")");
			return;
		}
		
		sendHeaders(e);
		sendBody(e);
		e.close();
	}

	private void sendHeaders(HttpExchange e) throws IOException {
		e.getResponseHeaders().add("Content-Type", "application/json");
		e.getResponseHeaders().add("Content-Encoding", "UTF-8");
		e.sendResponseHeaders(200, 0);
	}

	private void sendBody(HttpExchange e) throws IOException {
		
		OutputStreamWriter writer = new OutputStreamWriter(e.getResponseBody(), "UTF-8");
		PrintWriter out = new PrintWriter(writer, true);
		
		out.print("{");
		Set<String> keyset = casts.keySet();
		for(String id : keyset) {
			Broadcast cast = casts.get(id);
			out.print(castToJson(cast));
		}
		out.print("}");
		out.flush();
		
		e.getResponseBody().close();
	}
	
	private String castToJson(Broadcast cast) {
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("\"%s\":{", cast.getId()));
		builder.append(String.format("\"lat\":%s,", cast.getLattitude()));
		builder.append(String.format("\"lng\":%s", cast.getLongitude()));
		builder.append("}");
		return builder.toString();
	}

	public String getHttpContextName() {
		return httpContextName;
	}

	public void setHttpContextName(String httpContextName) {
		this.httpContextName = httpContextName;
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
