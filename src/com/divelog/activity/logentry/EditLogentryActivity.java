package com.divelog.activity.logentry;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.divelog.R;
import com.divelog.db.DataSource;
import com.divelog.db.model.Divesite;

import java.util.List;

public class EditLogentryActivity extends Activity {
	
	DataSource dataSource;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_logentry_layout);
        dataSource = new DataSource(this);
        dataSource.open();

        List<Divesite> allDivesites = dataSource.getAllDivesites();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner siteSpinner = (Spinner) findViewById(R.id.edit_logentry_divesite);
        siteSpinner.setAdapter(adapter);

        for (Divesite divesite : allDivesites) {
            adapter.add(divesite.getName());
        }

        dataSource.close();
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
