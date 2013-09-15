package com.divelog.db;

import java.util.ArrayList;
import java.util.List;

import com.divelog.db.model.Divesite;
import com.divelog.db.model.Logentry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;

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

    public Logentry createLogentry(int num, Time date, int duration, String gas_type, int gas_in, int gas_used, int depth, Divesite divesite, String description) {
        ContentValues values = new ContentValues();
        values.put("date", date.format2445());
        values.put("duration", duration);
        values.put("gas_type", gas_type);
        values.put("gas_in", gas_in);
        values.put("gas_used", gas_used);
        values.put("depth", depth);
        values.put("divesite", divesite.getId());
        values.put("description", description);
        database.insert(DataSourceHelper.TABLE_LOGENTRIES, null, values);

        return new Logentry(num, date, duration, gas_type, gas_in, gas_used, depth, divesite, description);
    }

    public Logentry getLogentry(int id) {
        Cursor cursor = database.query(DataSourceHelper.TABLE_LOGENTRIES, null, "id = " + id, null, null, null, null);
        cursor.moveToFirst();

        return extractLogentry(cursor);
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

    public List<Logentry> getAllLogentries() {
        List<Logentry> logentries = new ArrayList<Logentry>();
        Cursor cursor = database.query(DataSourceHelper.TABLE_LOGENTRIES, null, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            logentries.add(extractLogentry(cursor));
            cursor.moveToNext();
        }

        cursor.close();

        return logentries;
    }


    private Logentry extractLogentry(Cursor cursor) {
        Time time = new Time();

        int num = cursor.getInt(DataSourceHelper.LOGENTRIES_NUM_COLUMN);
        time.parse(cursor.getString(DataSourceHelper.LOGENTRIES_DATE_COLUMN));
        int duration = cursor.getInt(DataSourceHelper.LOGENTRIES_DURATION_COLUMN);
        String gas_type = cursor.getString(DataSourceHelper.LOGENTRIES_GAS_TYPE_COLUMN);
        int gas_in = cursor.getInt(DataSourceHelper.LOGENTRIES_GAS_IN_COLUMN);
        int gas_used = cursor.getInt(DataSourceHelper.LOGENTRIES_GAS_USED_COLUMN);
        int depth = cursor.getInt(DataSourceHelper.LOGENTRIES_DEPTH_COLUMN);
        int divesiteId = cursor.getInt(DataSourceHelper.LOGENTRIES_DIVESITE_COLUMN);
        String description = cursor.getString(DataSourceHelper.LOGENTRIES_DESCRIPTION_COLUMN);

        Divesite divesite = getDivesite(divesiteId);

        return new Logentry(num, time, duration, gas_type, gas_in, gas_used, depth, divesite, description);
    }
}
