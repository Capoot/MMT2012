package org.linesofcode.videoServer;

public class Broadcast {

	private String id;
	private long lattitude;
	private long longitude;
	
	public Broadcast(String id, long lattitude, long longitude) {
		this.id = id;
		this.lattitude = lattitude;
		this.longitude = longitude;
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
}
