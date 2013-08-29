package com.divelog.db.model;

import android.text.format.Time;

public class Logentry {

	private int num;
	private Time date;
    private int duration;
    private String gasType;
    private int gasIn;
    private int gasUsed;
    private int depth;
	private Divesite diveSite;
    private String description;
	
	public Logentry(int num, Time date, int duration, String gasType, int gasIn, int gasUsed, int depth, Divesite diveSite, String description) {
		this.num = num;
		this.date = date;
        this.duration = duration;
        this.gasType = gasType;
        this.gasIn = gasIn;
        this.gasUsed = gasUsed;
        this.depth = depth;
        this.diveSite = diveSite;
        this.description = description;
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

    public String getGasType() {
        return gasType;
    }

    public int getGasIn() {
        return gasIn;
    }

    public int getGasUsed() {
        return gasUsed;
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
