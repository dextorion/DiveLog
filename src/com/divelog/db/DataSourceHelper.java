package com.divelog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataSourceHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "divelog.db";
	public static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_LOGENTRIES = "logentries";
	public static final String LOGENTRY_NUM = "num";
	public static final String LOGENTRY_DATE = "date";
	public static final String LOGENTRY_DURATION = "duration";
	public static final String LOGENTRY_GAS_TYPE = "gas_type";
	public static final String LOGENTRY_GAS_IN = "gas_in";
	public static final String LOGENTRY_GAS_USED = "gas_used";
	public static final String LOGENTRY_DEPTH = "depth";
	public static final String LOGENTRY_DIVESITE = "divesite";
	public static final String LOGENTRY_DESCRIPTION ="description";
	
	public static final String QUERY_CREATE_TABLE_LOGENTRIES = "create table " + TABLE_LOGENTRIES + "(" 
			+ LOGENTRY_NUM + " integer primary key, "
			+ LOGENTRY_DATE + " datetime, "
			+ LOGENTRY_DURATION + " integer, "
			+ LOGENTRY_GAS_TYPE + " string, "
			+ LOGENTRY_GAS_IN + " integer, "
			+ LOGENTRY_GAS_USED + " integer, "
			+ LOGENTRY_DEPTH + " integer, "
			+ LOGENTRY_DIVESITE + " integer, "
			+ LOGENTRY_DESCRIPTION + " text"
			+ ");";
	
	public static final String TABLE_DIVESITES = "divesites";
	public static final String DIVESITE_ID = "id";
	public static final String DIVESITE_NAME = "name";
	public static final String DIVESITE_DESCRIPTION = "description";
	
	public static final String QUERY_CREATE_TABLE_DIVESITES = "create table " + TABLE_DIVESITES + "("
			+ DIVESITE_ID + " integer primary key autoincrement, "
			+ DIVESITE_NAME + " string, "
			+ DIVESITE_DESCRIPTION + " text"
			+ ");";
			
	public DataSourceHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(QUERY_CREATE_TABLE_LOGENTRIES);
		db.execSQL(QUERY_CREATE_TABLE_DIVESITES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists " + TABLE_LOGENTRIES);
		db.execSQL("drop table if exists " + TABLE_DIVESITES);
	    onCreate(db);
	}
}
