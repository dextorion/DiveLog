package com.divelog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataSourceHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "divelog.db";
	public static final int DATABASE_VERSION = 1;

	public static final String TABLE_LOGENTRIES = "logentries";
	public static final String QUERY_CREATE_TABLE_LOGENTRIES = "create table " + TABLE_LOGENTRIES + " (num integer primary key, date datetime, duration integer, gas_type string, gas_in integer, gas_used integer, depth integer, divesite integer, description text);";

	public static final String TABLE_DIVESITES = "divesites";
	public static final String QUERY_CREATE_TABLE_DIVESITES = "create table "+ TABLE_DIVESITES + "( id integer primary key autoincrement, name string, description text);";
	public static final String QUERY_SELECT_ALL_DIVESITES = "select * from divesites;";
	
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
