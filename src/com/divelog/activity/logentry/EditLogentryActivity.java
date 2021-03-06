package com.divelog.activity.logentry;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.divelog.R;
import com.divelog.db.DBUtil;
import com.divelog.db.model.Divesite;
import com.divelog.db.model.Logentry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EditLogentryActivity extends Activity {
	
	
	private TextView logentryDate; 
	
	private int year;
	private int month;
	private int day;
	
	private static final int DATE_DIALOG_ID = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_logentry_layout);
        
        setTitle("Edit log");
    }
	
	public void onResume() {
		super.onResume();
		
		logentryDate = (TextView)findViewById(R.id.edit_logentry_date);
		
		((EditText)findViewById(R.id.edit_logentry_num)).setText(String.valueOf(DBUtil.db.getNextLogentryNum()));

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner siteSpinner = (Spinner) findViewById(R.id.edit_logentry_divesite);
        siteSpinner.setAdapter(adapter);
        
        List<Divesite> allDivesites = DBUtil.db.getAllDivesites();
        for (Divesite divesite : allDivesites) {
            adapter.add(divesite.getName());
        }

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("id")) {
        	Logentry logentry = DBUtil.db.getLogentry(extras.getLong("id"));
        	
        	Divesite divesite = logentry.getDiveSite();
        	
        	int position = adapter.getPosition(divesite.getName());
        	siteSpinner.setSelection(position, true);
        	
        	Time date = logentry.getDate();
        	year = date.year;
        	month = date.month;
        	day = date.monthDay;
        	
        	((EditText)findViewById(R.id.edit_logentry_num)).setText(String.valueOf(logentry.getNum()));
        	((EditText)findViewById(R.id.edit_logentry_duration)).setText(String.valueOf(logentry.getDuration()));
    		((EditText)findViewById(R.id.edit_logentry_depth)).setText(String.valueOf(logentry.getDepth()));
    		((EditText)findViewById(R.id.edit_logentry_gasin)).setText(String.valueOf(logentry.getGasIn()));
    		((EditText)findViewById(R.id.edit_logentry_gasout)).setText(String.valueOf(logentry.getGasOut()));
    		((EditText)findViewById(R.id.edit_logentry_description)).setText(logentry.getDescription());

        } else {
	        final Calendar c = Calendar.getInstance();
	        year = c.get(Calendar.YEAR);
	        month = c.get(Calendar.MONTH)+1;
	        day = c.get(Calendar.DAY_OF_MONTH);
        }
        logentryDate.setText(new StringBuilder().append(year).append("-").append(month).append("-").append(day));
	}
	
	@Override
	public void onBackPressed() {
		int num = Integer.parseInt(((EditText)findViewById(R.id.edit_logentry_num)).getText().toString());

		Spinner siteSpinner = (Spinner)findViewById(R.id.edit_logentry_divesite);
		Divesite divesite = DBUtil.db.getDivesite((String)siteSpinner.getSelectedItem());

		EditText dateString = (EditText)findViewById(R.id.edit_logentry_date);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		
		Time date = new Time();
		try {
			String formatedDate = sdf.format(sdf.parse(dateString.getText().toString()));
			date.parse3339(formatedDate);
		} catch (Exception ex) {
			Toast.makeText(this, "Could not parse selected date", Toast.LENGTH_LONG).show();
			return;
		}

		String duration = ((EditText)findViewById(R.id.edit_logentry_duration)).getText().toString();
		String depth = ((EditText)findViewById(R.id.edit_logentry_depth)).getText().toString();
		String gasIn = ((EditText)findViewById(R.id.edit_logentry_gasin)).getText().toString();
		String gasOut = ((EditText)findViewById(R.id.edit_logentry_gasout)).getText().toString();
		String description = ((EditText)findViewById(R.id.edit_logentry_description)).getText().toString();
		
		if(!duration.isEmpty() && !depth.isEmpty()) {
			Bundle extras = getIntent().getExtras();
			if(extras != null && extras.containsKey("id")) {
				DBUtil.db.saveLogentry(extras.getLong("id"), num, date, Integer.parseInt(duration), gasIn.isEmpty() ? 0 : Integer.parseInt(gasIn), gasOut.isEmpty() ? 0 : Integer.parseInt(gasOut), Integer.parseInt(depth), divesite, description);
			} else {
				DBUtil.db.saveLogentry(num, date, Integer.parseInt(duration), gasIn.isEmpty() ? 0 : Integer.parseInt(gasIn), gasOut.isEmpty() ? 0 : Integer.parseInt(gasOut), Integer.parseInt(depth), divesite, description);
			}
			Toast.makeText(this, "Log saved", Toast.LENGTH_SHORT).show();
			
			getIntent().putExtra("num", num);
			setResult(RESULT_OK, getIntent());
		}
		
		finish();
	}
	
	public void showDatePickerDialog(View v) {
		showDialog(DATE_DIALOG_ID);
		
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
	   switch (id) {
	   case DATE_DIALOG_ID:
	      return new DatePickerDialog(this, dateSetListener, year, month, day);
	   }
	   return null;
	}
	
	private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
		
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            
            logentryDate.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day));
		}
	};
}
