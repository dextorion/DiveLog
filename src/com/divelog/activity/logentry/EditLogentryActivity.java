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
import com.divelog.db.DataSource;
import com.divelog.db.model.Divesite;
import com.divelog.db.model.Logentry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EditLogentryActivity extends Activity {
	
	DataSource dataSource;
	
	private TextView logentryDate; 
	
	private int year;
	private int month;
	private int day;
	
	private static final int DATE_DIALOG_ID = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_logentry_layout);
        
        dataSource = new DataSource(this);
        dataSource.open();

        logentryDate = (TextView)findViewById(R.id.edit_logentry_date);
        
        ((EditText)findViewById(R.id.edit_logentry_num)).setText(String.valueOf(dataSource.getNextLogentryNum()));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner siteSpinner = (Spinner) findViewById(R.id.edit_logentry_divesite);
        siteSpinner.setAdapter(adapter);
        
        List<Divesite> allDivesites = dataSource.getAllDivesites();
        for (Divesite divesite : allDivesites) {
            adapter.add(divesite.getName());
        }
        
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("id")) {
        	Logentry logentry = dataSource.getLogentry(extras.getInt("id"));
        	
        	Divesite divesite = logentry.getDiveSite();
        	
        	int position = adapter.getPosition(divesite.getName());
        	siteSpinner.setSelection(position, true);
        	
        	Time date = logentry.getDate();
        	year = date.year;
        	month = date.month;
        	day = date.monthDay;
        	
        	logentryDate.setText(new StringBuilder().append(year).append("-").append(month).append("-").append(day));
        	((EditText)findViewById(R.id.edit_logentry_num)).setText(String.valueOf(logentry.getNum()));
        	((EditText)findViewById(R.id.edit_logentry_duration)).setText(String.valueOf(logentry.getDuration()));
    		((EditText)findViewById(R.id.edit_logentry_depth)).setText(String.valueOf(logentry.getDepth()));
    		((EditText)findViewById(R.id.edit_logentry_gasin)).setText(String.valueOf(logentry.getGasIn()));
    		((EditText)findViewById(R.id.edit_logentry_gasout)).setText(String.valueOf(logentry.getGasOut()));
    		((EditText)findViewById(R.id.edit_logentry_description)).setText(logentry.getDescription());

        } else {
	        final Calendar c = Calendar.getInstance();
	        year = c.get(Calendar.YEAR);
	        month = c.get(Calendar.MONTH);
	        day = c.get(Calendar.DAY_OF_MONTH);
        }
    }
	
	public void saveLogentry(View v) {
		
		int num = Integer.parseInt(((EditText)findViewById(R.id.edit_logentry_num)).getText().toString());

		Spinner siteSpinner = (Spinner)findViewById(R.id.edit_logentry_divesite);
		Divesite divesite = dataSource.getDivesite((String)siteSpinner.getSelectedItem());

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

		int duration = Integer.parseInt(((EditText)findViewById(R.id.edit_logentry_duration)).getText().toString());
		int depth = Integer.parseInt(((EditText)findViewById(R.id.edit_logentry_depth)).getText().toString());
		int gasIn = Integer.parseInt(((EditText)findViewById(R.id.edit_logentry_gasin)).getText().toString());
		int gasOut = Integer.parseInt(((EditText)findViewById(R.id.edit_logentry_gasout)).getText().toString());
		String description = ((EditText)findViewById(R.id.edit_logentry_description)).getText().toString();
		
		dataSource.createLogentry(num, date, duration, gasIn, gasOut, depth, divesite, description);
		
		finish();
	}
	
	public void cancelLogentry(View v) {
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
