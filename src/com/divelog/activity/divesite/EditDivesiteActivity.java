package com.divelog.activity.divesite;

import com.divelog.R;
import com.divelog.db.DataSource;
import com.divelog.db.model.Divesite;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditDivesiteActivity extends Activity {
	
	DataSource dataSource;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_divesite_layout);
        dataSource = new DataSource(this);
        dataSource.open();
        
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("id")) {
        	Divesite divesite = dataSource.getDivesite(extras.getInt("id"));
        	((EditText)findViewById(R.id.edit_divesite_name)).setText(divesite.getName());;
    		((EditText)findViewById(R.id.edit_divesite_description)).setText(divesite.getDescription());;
        }
	}
	
	public void saveDivesite(View v) {
		
		EditText name = (EditText)findViewById(R.id.edit_divesite_name);
		EditText description = (EditText)findViewById(R.id.edit_divesite_description);
		
		Bundle extras = getIntent().getExtras();
		dataSource.saveDivesite(extras != null ? getIntent().getExtras().getInt("id") : null, name.getText().toString(), description.getText().toString());
		
		Toast.makeText(this, "Divesite saved", Toast.LENGTH_SHORT).show();
		
		finish();
	}
	
	public void cancelDivesite(View v) {
		finish();
	}
}
