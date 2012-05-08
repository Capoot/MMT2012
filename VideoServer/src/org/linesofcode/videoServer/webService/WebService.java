package org.linesofcode.videoServer.webService;

import java.io.IOException;

import org.linesofcode.videoServer.Broadcast;

public interface WebService {

	public void addCast(Broadcast cast);
	public void removeCast(Broadcast cast);
	public void start() throws IOException;
	public void stop();
}
