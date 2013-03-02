package com.divelog.db;

import com.divelog.model.Divesite;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataSource {

	private SQLiteDatabase database;
	private DataSourceHelper dbHelper;

	public DataSource(Context context) {
		dbHelper = new DataSourceHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public Divesite createDiveSite(String name, String description) {
		ContentValues values = new ContentValues();
		values.put(DataSourceHelper.DIVESITE_NAME, name);
		values.put(DataSourceHelper.DIVESITE_DESCRIPTION, description);
		long id = database.insert(DataSourceHelper.TABLE_DIVESITES, null, values);
		
		return new Divesite(id, name, description);
	}
	
}
