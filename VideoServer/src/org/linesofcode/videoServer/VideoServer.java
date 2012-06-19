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
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.linesofcode.videoServer.db.BroadcastDao;

public class VideoServer {
	
	private static final Logger LOG = Logger.getLogger(VideoServer.class);
	
	private String videoPath;
	private String dataDir;
	private String tempDir;
	private String ffmpegCmd;
	private String ffmpegOptionsArg;
	
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
	
	public void saveVideo(InputStream in, String id, double lat, double lng, String title, String ending) throws IOException, InterruptedException {
		LOG.debug("Transcoding video lat: " + lat + "; long: " + lng + "; ID: " + id + " title: " + title + "; data present: " + (in != null));
		String path = writeTemporaryVideoFileToDisk(in, id, ending);
		transcode(path, id, "mp4", "");
		addCast(new Broadcast(id, lat, lng, title));
		deleteTemporaryVideoFile(path);
		in.close();
	}

	private String writeTemporaryVideoFileToDisk(InputStream in, String id, String ending)
			throws FileNotFoundException, IOException {
		String path = String.format("%s/%s_temp%s", tempDir, id, ending);
		File file = new File(path);
		FileOutputStream fos = new FileOutputStream(file);
		BufferedOutputStream out = new BufferedOutputStream(fos);
		int read;
		do {
			byte[] data = new byte[512];
			read = in.read(data);
			out.write(data);
		} while(read > 0);
		out.close();
		return path;
	}
	
	private void transcode(String path, String id, String format, String options) throws IOException, InterruptedException {
		
		String ffmpegInArg = String.format("%s", new File(path).getAbsolutePath());
		String ffmpegOutArg = String.format("%s/%s.%s", new File(videoPath).getAbsolutePath(), id, format);
		
		LOG.debug(String.format("%s -i %s %s", ffmpegCmd, ffmpegInArg, ffmpegOutArg));
		
		ProcessBuilder pb = new ProcessBuilder();
		if(options.isEmpty()) {
			pb.command(ffmpegCmd, "-i", ffmpegInArg, ffmpegOutArg);
		} else {
			pb.command(ffmpegCmd, options, "-i", ffmpegInArg, ffmpegOutArg);
		}
		Process p = pb.start();
		
	    Scanner err = new Scanner(p.getErrorStream());
	    StringBuilder msg = new StringBuilder();
	    while (err.hasNextLine()) {
	        msg.append(err.nextLine()+"\n");
	    }

	    int returnCode = p.waitFor();
	    if(returnCode != 0) {
	    	System.err.println(msg.toString());
	    	throw new RuntimeException(String.format("Error transcoding video: %d", returnCode));
	    }
	}
	
	private void deleteTemporaryVideoFile(String path) {
		File file = new File(path);
		if(!file.delete()) {
			throw new RuntimeException("Failed to delete temporary video file");
		}
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

	public String getTempDir() {
		return tempDir;
	}

	public void setTempDir(String tempDir) {
		this.tempDir = tempDir;
	}

	public String getFfmpegCmd() {
		return ffmpegCmd;
	}

	public void setFfmpegCmd(String ffmpegCmd) {
		this.ffmpegCmd = ffmpegCmd;
	}

	public String getFfmpegOptionsArg() {
		return ffmpegOptionsArg;
	}

	public void setFfmpegOptionsArg(String ffmpegOptionsArg) {
		this.ffmpegOptionsArg = ffmpegOptionsArg;
	}
}
