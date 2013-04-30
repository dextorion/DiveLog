package com.divelog.activity.divesite;

import com.divelog.R;
import com.divelog.db.DataSource;
import com.divelog.db.model.Divesite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class ViewDivesiteActivity extends Activity {
	
	DataSource dataSource;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.view_divesite_layout);
        dataSource = new DataSource(this);
        dataSource.open();
        
        Bundle bundle = getIntent().getExtras();
        Divesite site = dataSource.getDivesite(bundle.getInt("id"));
        
        ((TextView)findViewById(R.id.view_divesite_name)).setText(site.getName());
        ((TextView)findViewById(R.id.view_divesite_description)).setText(site.getDescription());
	}
	
	
	public void editDivesite(View view) {
		//TODO: set divesite id
		startActivity(new Intent(this, EditDivesiteActivity.class));
	}
}
