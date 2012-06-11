package org.linesofcode.videoServer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.linesofcode.videoServer.db.BroadcastDao;

public class VideoServer {
	
	private static final Logger LOG = Logger.getLogger(VideoServer.class);
	
	private String videoPath;
	private String dataDir;
	
	private BroadcastDao broadcastDao;
	
	public void start() {
		
	}

	public void shutDown() {
		
	}

	public void addCast(Broadcast cast) {
		broadcastDao.persist(cast);
	}

	public void removeCast(Broadcast cast) {
		broadcastDao.delete(cast);
	}
	
	public Collection<Broadcast> getCasts() {
		return broadcastDao.listAll();
	}
	
	public InputStream loadVideo(String videoId) throws IOException {
		String path = String.format("%s/%s.mp4", videoPath, videoId);
		File file = new File(path);
		FileInputStream fis = new FileInputStream(file);
		return new BufferedInputStream(fis);
	}
	
	public void saveVideo(InputStream in, String id, double lat, double lng, String title) throws IOException {
		LOG.debug("Transcoding video lat: " + lat + "; long: " + lng + "; ID: " + id + " title: " + title + "; data present: " + (in != null));
		addCast(new Broadcast(id, lat, lng, title));
		writeVideoFileToDisk(in, id);
		in.close();
	}

	private void writeVideoFileToDisk(InputStream in, String id)
			throws FileNotFoundException, IOException {
		// TODO ensure vide content type
		File file = new File(String.format("%s/%s", videoPath, id));
		FileOutputStream fos = new FileOutputStream(file);
		BufferedOutputStream out = new BufferedOutputStream(fos);
		int read;
		do {
			byte[] data = new byte[512];
			read = in.read(data);
			out.write(data);
		} while(read > 0);
		out.close();
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

	public BroadcastDao getBroadcastDao() {
		return broadcastDao;
	}

	public void setBroadcastDao(BroadcastDao broadcastDao) {
		this.broadcastDao = broadcastDao;
	}
}
