package org.linesofcode.videoServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class HttpBroadcaster implements Broadcaster, HttpHandler {

	private static final Logger LOG = Logger.getLogger(HttpBroadcaster.class);
	private HttpServer server;
	private InetSocketAddress address;
	private String httpContextName;
	private HttpContext context;
	
	public HttpBroadcaster(int port, String httpContext) {
		address = new InetSocketAddress(port);
		this.httpContextName = httpContext;
	}
	
	@Override
	public void cleanup() {
		server.stop(0);
		LOG.info("Http Broadcasting Server stopped");
	}

	@Override
	public void initialize() throws IOException {
		server = HttpServer.create(address, 0);
		context = server.createContext(httpContextName, this);
		server.setExecutor(Executors.newCachedThreadPool());
	    server.start();
	    LOG.info("Http Broadcasting Server launched at port " + address.getPort() + ", context root: " + context.getPath());
	}

	@Override
	public void handle(HttpExchange e) throws IOException {
		
		if(!e.getRequestMethod().toUpperCase().equals("GET")) {
			e.sendResponseHeaders(405, 0);
			e.close();
			LOG.debug("Rejected request from host: " + e.getRemoteAddress() + " (unallowed method " + e.getRequestMethod() + ")");
			return;
		}
		
		// TODO find desired video and deliver
		LOG.debug("Host: " + e.getRemoteAddress().getAddress() + " requests resource " + e.getRequestURI() + " - delivering now...");
		
		e.sendResponseHeaders(501, 0);
		e.close();
	}
}
