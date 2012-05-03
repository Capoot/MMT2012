package org.linesofcode.videoServer.broadcast;

import java.io.IOException;

import org.apache.log4j.Logger;


public class RtpBroadcaster implements Broadcaster {

	private static final Logger LOG = Logger.getLogger(RtpBroadcaster.class);
	
	private int port;
	
	public RtpBroadcaster(int port) {
		this.port = port;
	}
	
	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		LOG.debug("RTP comming up at port " + port);
		
	}

	@Override
	public void initialize() throws IOException {
		// TODO Auto-generated method stub
		
	}

}
