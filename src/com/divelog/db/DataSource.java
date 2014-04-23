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
	
	public Divesite saveDivesite(String name, String description) {
		return saveDivesite(null, name, description);
	}
	
	public Divesite saveDivesite(Integer id, String name, String description) {
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("description", description);
		
		Divesite divesite = null;
		if(id != null && id != 0) {
			database.update(DataSourceHelper.TABLE_DIVESITES, values, "id = ?", new String[] {id.toString()});
			divesite = new Divesite(id, name, description);
		} else {
			Long newId = database.insert(DataSourceHelper.TABLE_DIVESITES, null, values);
			divesite = new Divesite(newId, name, description);
		}
		
		return divesite;
	}
	
	public Divesite getDivesite(int id) {
		Cursor cursor = database.query(DataSourceHelper.TABLE_DIVESITES, null, "id = " + id, null, null, null, null);
		cursor.moveToFirst();
		Divesite divesite = new Divesite(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
		
		return divesite;
	}
	
	public Divesite getDivesite(String name) {
		Cursor cursor = database.query(DataSourceHelper.TABLE_DIVESITES, null, "name = '" + name + "'", null, null, null, null);
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

	public Logentry createLogentry(int num, Time date, int duration, int gas_in, int gas_out, int depth, Divesite divesite, String description) {
        ContentValues values = new ContentValues();
        values.put("num", num);
        values.put("date", date.format2445());
        values.put("duration", duration);
        values.put("gas_in", gas_in);
        values.put("gas_out", gas_out);
        values.put("depth", depth);
        values.put("divesite", divesite.getId());
        values.put("description", description);
        long id = database.insert(DataSourceHelper.TABLE_LOGENTRIES, null, values);

        return new Logentry(id, num, date, duration, gas_in, gas_out, depth, divesite, description);
    }

	public int getNextLogentryNum() {
		Cursor cursor = database.query(DataSourceHelper.TABLE_LOGENTRIES, new String[]{"num"}, null, null, null, null, "num DESC", "1");
		cursor.moveToFirst();
		return cursor.getCount() != 0 ? cursor.getInt(0)+1 : 1;
	}
	
    public Logentry getLogentry(int id) {
        Cursor cursor = database.query(DataSourceHelper.TABLE_LOGENTRIES, null, "num = " + id, null, null, null, null);
        cursor.moveToFirst();

        Logentry logentry = extractLogentry(cursor);
        cursor.close();
        return logentry;
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

        int id = cursor.getInt(DataSourceHelper.LOGENTRY_ID_COLUMN);
        int num = cursor.getInt(DataSourceHelper.LOGENTRY_NUM_COLUMN);
        time.parse(cursor.getString(DataSourceHelper.LOGENTRY_DATE_COLUMN));
        int duration = cursor.getInt(DataSourceHelper.LOGENTRY_DURATION_COLUMN);
        int gas_in = cursor.getInt(DataSourceHelper.LOGENTRY_GAS_IN_COLUMN);
        int gas_out = cursor.getInt(DataSourceHelper.LOGENTRY_GAS_OUT_COLUMN);
        int depth = cursor.getInt(DataSourceHelper.LOGENTRY_DEPTH_COLUMN);
        int divesiteId = cursor.getInt(DataSourceHelper.LOGENTRY_DIVESITE_COLUMN);
        String description = cursor.getString(DataSourceHelper.LOGENTRY_DESCRIPTION_COLUMN);

        Divesite divesite = getDivesite(divesiteId);

        return new Logentry(id, num, time, duration, gas_in, gas_out, depth, divesite, description);
    }
}
