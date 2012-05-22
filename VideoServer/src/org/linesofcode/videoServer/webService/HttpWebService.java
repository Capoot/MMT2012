package org.linesofcode.videoServer.webService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.linesofcode.videoServer.Broadcast;

import com.sun.net.httpserver.HttpServer;

public class HttpWebService implements WebService {

	private static final Logger LOG = Logger.getLogger(HttpWebService.class);
	
	private HttpServer server;
	private InetSocketAddress address;
	private String listContextName;
	private String deliveryContextName;
	private int port;
	private String hostName;
	private VideoListHandler videoListHandler;
	private VideoDeliveryHandler deliveryHandler;
	
	private Map<String, Broadcast> casts = new HashMap<String, Broadcast>();

	public HttpWebService(VideoListHandler videoListHandler, VideoDeliveryHandler deliveryHandler) {
		this.videoListHandler = videoListHandler;
		this.deliveryHandler = deliveryHandler;
	}
	
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

		initHandlers();
		address = new InetSocketAddress(port);
		server = HttpServer.create(address, 0);
		server.createContext(listContextName, videoListHandler);
		server.createContext(deliveryContextName, deliveryHandler);
		server.setExecutor(Executors.newCachedThreadPool());
		
		LOG.info("Launching Http Web Service at port " + address.getPort() + ", context root: " + listContextName);
	    server.start();
	    
	    // FIXME this is testdata
	    addCast(new Broadcast("dummy", 52, 13));
	}

	private void initHandlers() {
		videoListHandler.setCasts(casts);
		videoListHandler.setHostPort(String.format("%s:%d", hostName, port));
	}

	@Override
	public void stop() {
		server.stop(0);
	}

	public String getListContextName() {
		return listContextName;
	}

	public void setListContextName(String listContextName) {
		this.listContextName = listContextName;
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getDeliveryContextName() {
		return deliveryContextName;
	}

	public void setDeliveryContextName(String deliveryContextName) {
		this.deliveryContextName = deliveryContextName;
	}
}
