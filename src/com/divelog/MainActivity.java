package com.divelog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.divelog.activity.divesite.DivesiteListActivity;
import com.divelog.activity.logentry.LogentryListActivity;
import com.divelog.db.DBUtil;

public class MainActivity extends Activity {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        setTitle("Dive Log");
        
        new DBUtil(this);
	}
	
	public void startDivesiteListActivity(View view) {
		startActivity(new Intent(this, DivesiteListActivity.class));
	}
	
	public void startLogentryListActivity(View view) {
		startActivity(new Intent(this, LogentryListActivity.class));
	}
	
	@Override
	public void onBackPressed() {
		DBUtil.db.close();
		System.exit(0);
	}
}
