package com.divelog.db.model;

import android.text.format.Time;

public class Logentry {

	private int id;
	private Time date;
	private String diveSite;
	
	public Logentry(int id, Time date, String diveSite) {
		this.id = id;
		this.date = date;
		this.diveSite = diveSite;
	}
	
	public int getId() {
		return id;
	}
	
	public Time getDate() {
		return date;
	}
	
	public String getDiveSite() {
		return diveSite;
	}
}
