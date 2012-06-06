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
	private String uploadContextName;
	private int port;
	private String hostName;
	private VideoListHandler videoListHandler;
	private VideoDeliveryHandler deliveryHandler;
	private VideoUploadHandler uploadHandler;

	public RestAPI(VideoListHandler videoListHandler, VideoDeliveryHandler deliveryHandler, VideoUploadHandler uploadHandler) {
		this.videoListHandler = videoListHandler;
		this.deliveryHandler = deliveryHandler;
		this.uploadHandler = uploadHandler;
	}
	
	public void start() throws IOException {

		initHandlers();
		address = new InetSocketAddress(port);
		server = HttpServer.create(address, 0);
		server.setExecutor(Executors.newCachedThreadPool());
		server.createContext(listContextName, videoListHandler);
		server.createContext(deliveryContextName, deliveryHandler);
		server.createContext(uploadContextName, uploadHandler);
	    server.start();
	    LOG.info("REST API running at port " + address.getPort());
	}

	private void initHandlers() {
		videoListHandler.setHostPort(String.format("%s:%d", hostName, port));
	}

	public void stop() {
		server.stop(0);
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
	
	public String getListContextName() {
		return listContextName;
	}

	public void setListContextName(String listContextName) {
		this.listContextName = listContextName;
	}

	public String getDeliveryContextName() {
		return deliveryContextName;
	}

	public void setDeliveryContextName(String deliveryContextName) {
		this.deliveryContextName = deliveryContextName;
	}

	public String getUploadContextName() {
		return uploadContextName;
	}

	public void setUploadContextName(String uploadContextName) {
		this.uploadContextName = uploadContextName;
	}
}
