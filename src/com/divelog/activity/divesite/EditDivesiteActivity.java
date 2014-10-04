package com.divelog.activity.divesite;

import com.divelog.R;
import com.divelog.db.DBUtil;
import com.divelog.db.model.Divesite;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class EditDivesiteActivity extends Activity {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_divesite_layout);
        
        setTitle("Edit divesite");
        
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("id")) {
        	Divesite divesite = DBUtil.db.getDivesite(extras.getInt("id"));
        	((EditText)findViewById(R.id.edit_divesite_name)).setText(divesite.getName());
    		((EditText)findViewById(R.id.edit_divesite_description)).setText(divesite.getDescription());
        }
	}
	
	@Override
	public void onBackPressed() {
		EditText name = (EditText)findViewById(R.id.edit_divesite_name);
		EditText description = (EditText)findViewById(R.id.edit_divesite_description);
		
		Bundle extras = getIntent().getExtras();
		DBUtil.db.saveDivesite(extras != null ? getIntent().getExtras().getInt("id") : null, name.getText().toString(), description.getText().toString());
		
		Toast.makeText(this, "Divesite saved", Toast.LENGTH_SHORT).show();
		
		finish();
	}
}
