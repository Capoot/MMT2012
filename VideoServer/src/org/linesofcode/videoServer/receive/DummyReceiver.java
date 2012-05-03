package org.linesofcode.videoServer.receive;

public class DummyReceiver implements Receiver {

	@Override
	public void cleanup() {
		// dummy is dumb
	}

	@Override
	public void initialize() {
		// dummy cannot do anything. sad, if you come to think of it...
	}

}
