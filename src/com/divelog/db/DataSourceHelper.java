package com.divelog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataSourceHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "divelog.db";
	public static final int DATABASE_VERSION = 1;

	public static final String TABLE_LOGENTRIES = "logentries";
	public static final String QUERY_CREATE_TABLE_LOGENTRIES = "create table " + TABLE_LOGENTRIES + " (num integer primary key, date datetime, duration integer, gas_type string, gas_in integer, gas_used integer, depth integer, divesite integer, description text);";

    public static final int LOGENTRIES_NUM_COLUMN = 0;
    public static final int LOGENTRIES_DATE_COLUMN = 1;
    public static final int LOGENTRIES_DURATION_COLUMN = 2;
    public static final int LOGENTRIES_GAS_TYPE_COLUMN = 3;
    public static final int LOGENTRIES_GAS_IN_COLUMN = 4;
    public static final int LOGENTRIES_GAS_USED_COLUMN = 5;
    public static final int LOGENTRIES_DEPTH_COLUMN = 6;
    public static final int LOGENTRIES_DIVESITE_COLUMN = 7;
    public static final int LOGENTRIES_DESCRIPTION_COLUMN = 8;

	public static final String TABLE_DIVESITES = "divesites";
	public static final String QUERY_CREATE_TABLE_DIVESITES = "create table "+ TABLE_DIVESITES + "( id integer primary key autoincrement, name string, description text);";

	
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
