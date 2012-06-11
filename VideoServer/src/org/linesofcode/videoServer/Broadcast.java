package org.linesofcode.videoServer;

import java.io.Serializable;

public class Broadcast implements Serializable {

	private static final long serialVersionUID = 5921215050421366377L;
	
	private String id;
	private double lattitude;
	private double longitude;
	private String title;
	
	public Broadcast(String id, double lattitude, double longitude, String title) {
		this.id = id;
		this.lattitude = lattitude;
		this.longitude = longitude;
		this.title = title;
	}

	public void setLattitude(double lattitude) {
		this.lattitude = lattitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLattitude() {
		return lattitude;
	}

	public double getLongitude() {
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
