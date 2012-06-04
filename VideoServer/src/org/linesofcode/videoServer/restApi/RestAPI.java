package org.linesofcode.videoServer.restApi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.sun.net.httpserver.HttpServer;

public class RestAPI {

	private static final Logger LOG = Logger.getLogger(RestAPI.class);
	
	private HttpServer server;
	private InetSocketAddress address;
	private String listContextName;
	private String deliveryContextName;
	private int port;
	private String hostName;
	private VideoListHandler videoListHandler;
	private VideoDeliveryHandler deliveryHandler;

	public RestAPI(VideoListHandler videoListHandler, VideoDeliveryHandler deliveryHandler) {
		this.videoListHandler = videoListHandler;
		this.deliveryHandler = deliveryHandler;
	}
	
	public void start() throws IOException {

		initHandlers();
		address = new InetSocketAddress(port);
		server = HttpServer.create(address, 0);
		server.createContext(listContextName, videoListHandler);
		server.createContext(deliveryContextName, deliveryHandler);
		server.setExecutor(Executors.newCachedThreadPool());
		
	    server.start();
	    LOG.info("REST API running at port " + address.getPort());
	}

	private void initHandlers() {
		videoListHandler.setHostPort(String.format("%s:%d", hostName, port));
	}

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
