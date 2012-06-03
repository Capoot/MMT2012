package org.linesofcode.videoServer;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class VideoServer {
	
	
	private Map<String, Broadcast> casts = new HashMap<String, Broadcast>();
	
	public VideoServer() {
	}
	
	public void start() throws IOException {
		// FIXME this is testdata
	    addCast(new Broadcast("dummy", 52, 13));
	}

	public void shutDown() throws IOException {
	}

	public void addCast(Broadcast cast) {
		casts.put(cast.getId(), cast);
	}

	public void removeCast(Broadcast cast) {
		casts.remove(cast.getId());
	}
	
	public Collection<Broadcast> getCasts() {
		return casts.values();
	}
}
