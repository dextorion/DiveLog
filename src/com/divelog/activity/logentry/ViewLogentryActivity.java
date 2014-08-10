package com.divelog.activity.logentry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.divelog.R;
import com.divelog.db.DBUtil;
import com.divelog.db.model.Logentry;


public class ViewLogentryActivity extends Activity {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.view_logentry_layout);
        
        Bundle bundle = getIntent().getExtras();
        Logentry entry = DBUtil.db.getLogentry(bundle.getInt("id"));
        
        ((TextView)findViewById(R.id.view_logentry_num)).setText(String.valueOf(entry.getNum()));
        ((TextView)findViewById(R.id.view_logentry_date)).setText(String.valueOf(entry.getDate().format("%Y-%m-%d")));
        ((TextView)findViewById(R.id.view_logentry_divesite)).setText(entry.getDiveSite().getName());
        ((TextView)findViewById(R.id.view_logentry_duration)).setText(String.valueOf(entry.getDuration()));
        ((TextView)findViewById(R.id.view_logentry_depth)).setText(String.valueOf(entry.getDepth()));
	}
	
	@Override
    public void onResume() {
        super.onResume();

        Bundle bundle = getIntent().getExtras();
        Logentry entry = DBUtil.db.getLogentry(bundle.getInt("id"));
        
        ((TextView)findViewById(R.id.view_logentry_num)).setText(String.valueOf(entry.getNum()));
        ((TextView)findViewById(R.id.view_logentry_description)).setText(entry.getDescription());
    }
	
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logentry_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
            case R.id.logentry_edit_entry:
            	Intent editLogentryIntent = new Intent(this, EditLogentryActivity.class);
                editLogentryIntent.putExtras(getIntent().getExtras());
                startActivity(editLogentryIntent);
                return true;
            	
            default:
            	return super.onOptionsItemSelected(item);
        }
    }
}
