package com.divelog.activity.logentry;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.divelog.R;
import com.divelog.db.DataSource;

public class EditLogentryActivity extends Activity {
	
	DataSource dataSource;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_divesite_layout);
        dataSource = new DataSource(this);
        dataSource.open();
	}
	
	public void saveDivesite(View v) {
		
		EditText name = (EditText)findViewById(R.id.edit_divesite_name);
		EditText description = (EditText)findViewById(R.id.edit_divesite_description);
		
		dataSource.createDiveSite(name.getText().toString(), description.getText().toString());
		
		Toast.makeText(this, "Divesite saved", Toast.LENGTH_SHORT).show();
		
		finish();
	}
	
	public void cancelDivesite(View v) {
		finish();
	}
}
