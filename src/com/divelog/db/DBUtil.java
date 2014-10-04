package com.divelog.db;

import java.util.ArrayList;
import java.util.List;

import com.divelog.db.model.Divesite;
import com.divelog.db.model.Logentry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;

public class DBUtil extends SQLiteOpenHelper {

	public static DBUtil db = null;
	
	public static final String DATABASE_NAME = "divelog.db";
	public static final int DATABASE_VERSION = 1;

	public static final String TABLE_LOGENTRIES = "logentries";
	public static final String QUERY_CREATE_TABLE_LOGENTRIES = "create table " + TABLE_LOGENTRIES + " (id integer primary key autoincrement, num integer, date datetime, duration integer, gas_in integer, gas_out integer, depth integer, divesite integer, description text);";

	public static final int LOGENTRY_ID_COLUMN = 0;
    public static final int LOGENTRY_NUM_COLUMN = 1;
    public static final int LOGENTRY_DATE_COLUMN = 2;
    public static final int LOGENTRY_DURATION_COLUMN = 3;
    public static final int LOGENTRY_GAS_IN_COLUMN = 4;
    public static final int LOGENTRY_GAS_OUT_COLUMN = 5;
    public static final int LOGENTRY_DEPTH_COLUMN = 6;
    public static final int LOGENTRY_DIVESITE_COLUMN = 7;
    public static final int LOGENTRY_DESCRIPTION_COLUMN = 8;

	public static final String TABLE_DIVESITES = "divesites";
	public static final String QUERY_CREATE_TABLE_DIVESITES = "create table "+ TABLE_DIVESITES + "( id integer primary key autoincrement, name string, description text);";

	
	public DBUtil(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		db = this;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(QUERY_CREATE_TABLE_LOGENTRIES);
		db.execSQL(QUERY_CREATE_TABLE_DIVESITES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		if(oldVersion < newVersion) {
			switch(oldVersion) {
				case 1:
					//insert update sql from version 1 here
				
				default:
					db.execSQL("drop table if exists " + TABLE_LOGENTRIES);
					db.execSQL("drop table if exists " + TABLE_DIVESITES);
					onCreate(db);
				break;
			}
		}
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
			getWritableDatabase().update(DBUtil.TABLE_DIVESITES, values, "id = ?", new String[] {id.toString()});
			divesite = new Divesite(id, name, description);
		} else {
			Long newId = getWritableDatabase().insert(DBUtil.TABLE_DIVESITES, null, values);
			divesite = new Divesite(newId, name, description);
		}
		
		return divesite;
	}
	
	public Divesite getDivesite(int id) {
		Cursor cursor = getWritableDatabase().query(DBUtil.TABLE_DIVESITES, null, "id = " + id, null, null, null, null);
		cursor.moveToFirst();
		Divesite divesite = new Divesite(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
		
		return divesite;
	}
	
	public Divesite getDivesite(String name) {
		Cursor cursor = getWritableDatabase().query(DBUtil.TABLE_DIVESITES, null, "name = '" + name + "'", null, null, null, null);
		cursor.moveToFirst();
		Divesite divesite = new Divesite(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
		
		return divesite;
	}

	public List<Divesite> getAllDivesites() {
		List<Divesite> divesites = new ArrayList<Divesite>();
		Cursor cursor = getWritableDatabase().query(DBUtil.TABLE_DIVESITES, null, null, null, null, null, null);
		
		cursor.moveToFirst();
		while ( !cursor.isAfterLast()) {
			divesites.add(new Divesite(cursor.getLong(0), cursor.getString(1), cursor.getString(2)));
			cursor.moveToNext();
		}
		
		cursor.close();
		
		return divesites;
	}
	
	public void deleteDivesite(int id) {
		getWritableDatabase().delete(DBUtil.TABLE_DIVESITES, "id = ?", new String[] {String.valueOf(id)});
	}

	public int getNextLogentryNum() {
		Cursor cursor = getWritableDatabase().query(DBUtil.TABLE_LOGENTRIES, new String[]{"num"}, null, null, null, null, "num DESC", "1");
		cursor.moveToFirst();
		return cursor.getCount() != 0 ? cursor.getInt(0)+1 : 1;
	}
	
	public Logentry saveLogentry(int num, Time date, int duration, int gasIn, int gasOut, int depth, Divesite divesite, String description) {
		return saveLogentry(null, num, date, duration, gasIn, gasOut, depth, divesite, description);
	}
	
	public Logentry saveLogentry(Long id, int num, Time date, int duration, int gasIn, int gasOut, int depth, Divesite divesite, String description) {
		
		SQLiteDatabase database = getWritableDatabase();

		ContentValues values = new ContentValues();
        values.put("num", num);
        values.put("date", date.format2445());
        values.put("duration", duration);
        values.put("gas_in", gasIn);
        values.put("gas_out", gasOut);
        values.put("depth", depth);
        values.put("divesite", divesite.getId());
        values.put("description", description);
        
        Logentry logentry = null;
        
		if(id != null && id != 0) {
			
			if(getLogentryByNum(num) != null) {
				ContentValues numvalues = new ContentValues();
				Logentry currentEntry = getLogentry(id);
				
				if(currentEntry.getNum() < num) {
					List<Logentry> entries = getLogentries(currentEntry.getNum(), num);
					
					for(int i = 0; i < entries.size(); i++) {
						Logentry entry = entries.get(i);
						if(entry.getNum() == currentEntry.getNum()+i) {
							numvalues.clear();
							numvalues.put("num", currentEntry.getNum()+i-1);
							database.update(DBUtil.TABLE_LOGENTRIES, numvalues, "id = ?", new String[] {String.valueOf(entry.getId())});
						} else {
							break;
						}
					}
					
				}
				
				if(num < currentEntry.getNum()) {
					List<Logentry> entries = getLogentries(num, currentEntry.getNum());
					
					for(int i = 0; i < entries.size(); i++) {
						Logentry entry = entries.get(i);
						if(entry.getNum() == num+i) {
							numvalues.clear();
							numvalues.put("num", num+i+1);
							database.update(DBUtil.TABLE_LOGENTRIES, numvalues, "id = ?", new String[] {String.valueOf(entry.getId())});
						} else {
							break;
						}
					}
					
				}
			}
			
			database.update(DBUtil.TABLE_LOGENTRIES, values, "id = ?", new String[] {id.toString()});
			logentry = new Logentry(id, num, date, duration, gasIn, gasOut, depth, divesite, description);
			
		} else {
			if(getLogentryByNum(num) != null) {
				
				ContentValues numvalues = new ContentValues();
				int nextNum = getNextLogentryNum();
				if(num < nextNum) {
					List<Logentry> entries = getLogentries(num, nextNum);
					
					for(int i = 0; i < entries.size(); i++) {
						Logentry entry = entries.get(i);
						if(entry.getNum() == num+i) {
							numvalues.clear();
							numvalues.put("num", num+i+1);
							database.update(DBUtil.TABLE_LOGENTRIES, numvalues, "id = ?", new String[] {String.valueOf(entry.getId())});
						} else {
							break;
						}
					}
				}
			}
			
			Long newId = database.insert(DBUtil.TABLE_LOGENTRIES, null, values);
			logentry = new Logentry(newId, num, date, duration, gasIn, gasOut, depth, divesite, description);
		}
        
        return logentry;
	}
	
	 public Logentry getLogentry(long id) {
        Cursor cursor = getWritableDatabase().query(DBUtil.TABLE_LOGENTRIES, null, "id = " + id, null, null, null, null);
        cursor.moveToFirst();

        Logentry logentry = extractLogentry(cursor);
        cursor.close();
        return logentry;
    }
	
    public Logentry getLogentryByNum(int num) {
        Cursor cursor = getWritableDatabase().query(DBUtil.TABLE_LOGENTRIES, null, "num = " + num, null, null, null, null);
        cursor.moveToFirst();

        Logentry logentry = extractLogentry(cursor);
        cursor.close();
        return logentry;
    }
    
    public List<Logentry> getLogentries(int from, int to) {
    	List<Logentry> logentries = new ArrayList<Logentry>();
        Cursor cursor = getWritableDatabase().query(DBUtil.TABLE_LOGENTRIES, null, "num >= "+from+" and num <= "+to, null, null, null, "num");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            logentries.add(extractLogentry(cursor));
            cursor.moveToNext();
        }

        cursor.close();

        return logentries;
    }

    public List<Logentry> getAllLogentries() {
        List<Logentry> logentries = new ArrayList<Logentry>();
        Cursor cursor = getWritableDatabase().query(DBUtil.TABLE_LOGENTRIES, null, null, null, null, null, "num");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            logentries.add(extractLogentry(cursor));
            cursor.moveToNext();
        }

        cursor.close();

        return logentries;
    }
    
    public void deleteLogentry(long id) {
    	Logentry currentEntry = getLogentry(id);
    	int num = getNextLogentryNum();
    	getWritableDatabase().delete(DBUtil.TABLE_LOGENTRIES, "id = ?", new String[] {String.valueOf(id)});
		
		List<Logentry> entries = getLogentries(currentEntry.getNum()+1, num);
		
		ContentValues numvalues = new ContentValues();
		for(int i = 0; i < entries.size(); i++) {
			Logentry entry = entries.get(i);
			if(entry.getNum() == currentEntry.getNum()+1+i) {
				numvalues.clear();
				numvalues.put("num", currentEntry.getNum()+i);
				getWritableDatabase().update(DBUtil.TABLE_LOGENTRIES, numvalues, "id = ?", new String[] {String.valueOf(entry.getId())});
			} else {
				break;
			}
		}
	}

    private Logentry extractLogentry(Cursor cursor) {
        if(cursor.getCount() == 0)
        	return null;
    	
    	Time time = new Time();

        int id = cursor.getInt(DBUtil.LOGENTRY_ID_COLUMN);
        int num = cursor.getInt(DBUtil.LOGENTRY_NUM_COLUMN);
        time.parse(cursor.getString(DBUtil.LOGENTRY_DATE_COLUMN));
        int duration = cursor.getInt(DBUtil.LOGENTRY_DURATION_COLUMN);
        int gas_in = cursor.getInt(DBUtil.LOGENTRY_GAS_IN_COLUMN);
        int gas_out = cursor.getInt(DBUtil.LOGENTRY_GAS_OUT_COLUMN);
        int depth = cursor.getInt(DBUtil.LOGENTRY_DEPTH_COLUMN);
        int divesiteId = cursor.getInt(DBUtil.LOGENTRY_DIVESITE_COLUMN);
        String description = cursor.getString(DBUtil.LOGENTRY_DESCRIPTION_COLUMN);

        Divesite divesite = getDivesite(divesiteId);

        return new Logentry(id, num, time, duration, gas_in, gas_out, depth, divesite, description);
    }
}
