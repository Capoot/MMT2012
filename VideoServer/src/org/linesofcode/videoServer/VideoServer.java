package org.linesofcode.videoServer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class VideoServer {
	
	private Map<String, Broadcast> casts = new HashMap<String, Broadcast>();
	
	private String videoPath;
	
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
	
	public InputStream loadVideo(String videoId) throws IOException {
		String path = String.format("%s/%s.mp4", videoPath, videoId);
		File file = new File(path);
		FileInputStream fis = new FileInputStream(file);
		return new BufferedInputStream(fis);
	}
	
	public void saveVideo(InputStream in, String id, double lat, double lng) throws IOException {
		in.close();
	}
	
	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}
}
