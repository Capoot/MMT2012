package org.linesofcode.videoServer.restApi.exception;

public class UnsupportedFormatException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String format;

	public UnsupportedFormatException(String ending) {
		super(String.format("Unsupported file format: %s", ending));
		format = ending;
	}

	public String getFormat() {
		return format;
	}

}
