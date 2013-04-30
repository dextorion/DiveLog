package com.divelog;

import com.divelog.activity.divesite.DivesiteListActivity;
import com.divelog.activity.logentry.LogentryListActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
	}
	
	public void startDivesiteListActivity(View view) {
		startActivity(new Intent(this, DivesiteListActivity.class));
	}
	
	public void startLogentryListActivity(View view) {
		startActivity(new Intent(this, LogentryListActivity.class));
	}
	
	public void exit(View view) {
		System.exit(0);
	}
}
