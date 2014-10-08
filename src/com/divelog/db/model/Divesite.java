package com.divelog.db.model;

public class Divesite {

	private long id;
	private String name;
	private String description;
	private Double longitude;
	private Double latitude;

	public Divesite(long id, String name, String description, Double longitude, Double latitude) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setLongitude(Double longitude)  {
		this.longitude = longitude;
	}
	
	public Double getLongitude() {
		return longitude;
	}
	
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	public Double getLatitude() {
		return latitude;
	}
}
