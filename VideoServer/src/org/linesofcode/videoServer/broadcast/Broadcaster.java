package org.linesofcode.videoServer.broadcast;

import java.io.IOException;

public interface Broadcaster {

	public void cleanup();

	public void initialize() throws IOException;

}
