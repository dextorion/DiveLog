package com.divelog.activity.logentry;

import com.divelog.db.DataSource;
import com.divelog.db.model.Divesite;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LogentrySettings extends Activity {
	
	DataSource dataSource;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        //setContentView(R.layout.edit_divesite_layout);
        //dataSource = new DataSource(this);
        //dataSource.open();
	}
	
	public void saveLogentrySettings(View v) {
		
		/*EditText name = (EditText)findViewById(R.id.edit_divesite_name);
		EditText description = (EditText)findViewById(R.id.edit_divesite_description);
		
		Divesite diveSite = dataSource.createDiveSite(name.getText().toString(), description.getText().toString());
		
		Toast.makeText(this, diveSite.getDescription(), Toast.LENGTH_SHORT).show();
		*/
	}
	
	public void cancelLogentrySettings(View v) {
		//Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
	}
}
