package org.linesofcode.videoServer;

import java.io.Serializable;

public class Broadcast implements Serializable {

	private static final long serialVersionUID = 5921215050421366377L;
	
	private String id;
	private long lattitude;
	private long longitude;
	private String title;
	
	public Broadcast(String id, long lattitude, long longitude, String title) {
		this.id = id;
		this.lattitude = lattitude;
		this.longitude = longitude;
		this.title = title;
	}

	public void setLattitude(long lattitude) {
		this.lattitude = lattitude;
	}

	public void setLongitude(long longitude) {
		this.longitude = longitude;
	}

	public String getUrl() {
		return id;
	}

	public long getLattitude() {
		return lattitude;
	}

	public long getLongitude() {
		return longitude;
	}

	public String getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
