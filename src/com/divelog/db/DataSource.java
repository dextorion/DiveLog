package com.divelog.db;

import java.util.ArrayList;
import java.util.List;

import com.divelog.db.model.Divesite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
		database.close();
		dbHelper.close();
	}
	
	public Divesite createDiveSite(String name, String description) {
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("description", description);
		long id = database.insert(DataSourceHelper.TABLE_DIVESITES, null, values);
		
		return new Divesite(id, name, description);
	}
	
	public Divesite getDivesite(int id) {
		Cursor cursor = database.query(DataSourceHelper.TABLE_DIVESITES, null, "id = " + id, null, null, null, null);
		cursor.moveToFirst();
		Divesite divesite = new Divesite(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
		
		return divesite;
	}
	
	public List<Divesite> getAllDivesites() {
		List<Divesite> divesites = new ArrayList<Divesite>();
		Cursor cursor = database.query(DataSourceHelper.TABLE_DIVESITES, null, null, null, null, null, null);
		
		cursor.moveToFirst();
		while ( !cursor.isAfterLast()) {
			divesites.add(new Divesite(cursor.getLong(0), cursor.getString(1), cursor.getString(2)));
			cursor.moveToNext();
		}
		
		cursor.close();
		
		return divesites;
	}
	
}
