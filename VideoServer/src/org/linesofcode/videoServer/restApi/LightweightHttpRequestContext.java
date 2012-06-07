package org.linesofcode.videoServer.restApi;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.fileupload.RequestContext;

import com.sun.net.httpserver.HttpExchange;

public class LightweightHttpRequestContext implements RequestContext {

	private String characterEncoding;
	private String contentType;
	private int contentLength;
	private InputStream stream;

	public LightweightHttpRequestContext(HttpExchange e) {
		
		List<String> enc = e.getRequestHeaders().get("Content-Encoding");
		if(enc != null) {
			characterEncoding = enc.get(0);
		}
		
		contentType = e.getRequestHeaders().get("Content-Type").get(0);
		contentLength = Integer.parseInt(e.getRequestHeaders().get("Content-Length").get(0));
		stream = e.getRequestBody();
	}
	
	@Override
	public String getCharacterEncoding() {
		return characterEncoding;
	}

	@Override
	public int getContentLength() {
		return contentLength;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return stream;
	}
}
