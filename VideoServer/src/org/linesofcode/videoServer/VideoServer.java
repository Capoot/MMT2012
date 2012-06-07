package org.linesofcode.videoServer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class VideoServer {
	
	private static final Logger LOG = Logger.getLogger(VideoServer.class);
	private static final String DATA_FILE = "broadcast_data.ser";
	
	private Map<String, Broadcast> casts = new HashMap<String, Broadcast>();
	private String videoPath;
	private String dataDir;
	
	public void start() {
		LOG.info("Loading broadcasts...");
		try {
			loadCasts();
		} catch(IOException e) {
			LOG.warn(String.format("Loading broadcast data from %s/%s failed. A new data file will be created. " +
					"Please check your config, if data should have been present.", dataDir, DATA_FILE));
			// error intentionally supressed
		}
	}

	private void loadCasts() throws IOException {
		File file = new File(String.format("%s/%s", dataDir, DATA_FILE));
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		ObjectInputStream in = new ObjectInputStream(bis);
		try {
			readBroadcasts(in);
		} catch (ClassNotFoundException e) {
			String message = String.format("SEVERE: %s. This indicates a corrupted installation. Please reinstall the server.",
					e.getMessage());
			LOG.error(message);
			throw new RuntimeException("Class not found when trying to load broadcasts", e);
		} finally {
			in.close();
		}
	}

	private void readBroadcasts(ObjectInputStream in) throws IOException, ClassNotFoundException {
		while(true) {
			Broadcast cast = (Broadcast)in.readObject();
			if(cast == null) {
				break;
			}
			casts.put(cast.getId(), cast);
		}
	}

	public void shutDown() {
		LOG.info("Saving broadcasts...");
		try {
			saveCasts();
		} catch(Exception e) {
			LOG.error(String.format("Error saving broadcast data %s", e));
		}
	}

	private void saveCasts() throws IOException {
		File file = new File(String.format("%s/%s", dataDir, DATA_FILE));
		FileOutputStream fos = new FileOutputStream(file);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		ObjectOutputStream out = new ObjectOutputStream(bos);
		try {
			writeBroadcasts(out);
		} finally {
			out.close();
		}
	}

	private void writeBroadcasts(ObjectOutputStream out) throws IOException {
		for(Broadcast cast : casts.values()) {
			out.writeObject(cast);
		}
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
	
	public void saveVideo(InputStream in, String id, double lat, double lng, String title) throws IOException {
		LOG.debug("Transcoding video lat: " + lat + "; long: " + lng + "; ID: " + id + " title: " + title + "; data present: " + (in != null));
		in.close();
	}
	
	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public String getDataDir() {
		return dataDir;
	}

	public void setDataDir(String dataDir) {
		this.dataDir = dataDir;
	}
}
