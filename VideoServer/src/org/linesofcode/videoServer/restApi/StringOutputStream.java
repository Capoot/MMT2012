package org.linesofcode.videoServer.restApi;

import java.io.IOException;
import java.io.OutputStream;

public class StringOutputStream extends OutputStream {

	private StringBuilder buffer;
	
	public StringOutputStream(StringBuilder buffer) {
		this.buffer = buffer;
	}
	
	@Override
	public void write(int b) throws IOException {
		buffer.append((char)b);
	}

}
