package com.divelog.activity.logentry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.divelog.R;
import com.divelog.db.DataSource;
import com.divelog.db.model.Logentry;


public class ViewLogentryActivity extends Activity {
	
	DataSource dataSource;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.view_logentry_layout);
        dataSource = new DataSource(this);
        dataSource.open();
        
        Bundle bundle = getIntent().getExtras();
        Logentry entry = dataSource.getLogentry(bundle.getInt("id"));
        
        ((TextView)findViewById(R.id.view_logentry_num)).setText(String.valueOf(entry.getNum()));
        ((TextView)findViewById(R.id.view_logentry_description)).setText(entry.getDescription());
	}
	
	
	public void editLogentry(View view) {
		//TODO: set logentry id
		startActivity(new Intent(this, EditLogentryActivity.class));
	}
}
