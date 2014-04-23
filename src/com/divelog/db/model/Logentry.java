package com.divelog.db.model;

import android.text.format.Time;

public class Logentry {

	private long id;
	private int num;
	private Time date;
    private int duration;
    private int gasIn;
    private int gasOut;
    private int depth;
	private Divesite diveSite;
    private String description;
	
	public Logentry(long id, int num, Time date, int duration, int gasIn, int gasOUt, int depth, Divesite diveSite, String description) {
		this.id = id;
		this.num = num;
		this.date = date;
        this.duration = duration;
        this.gasIn = gasIn;
        this.gasOut = gasOUt;
        this.depth = depth;
        this.diveSite = diveSite;
        this.description = description;
    }
	
	public long getId() {
		return id;
	}
	
	public int getNum() {
		return num;
	}
	
	public Time getDate() {
		return date;
	}

    public int getDuration() {
        return duration;
    }

    public int getGasIn() {
        return gasIn;
    }

    public int getGasOut() {
        return gasOut;
    }

    public int getDepth() {
        return depth;
    }

    public Divesite getDiveSite() {
		return diveSite;
	}

    public String getDescription() {
        return description;
    }
}
