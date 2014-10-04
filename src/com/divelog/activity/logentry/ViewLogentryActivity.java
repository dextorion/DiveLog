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
	
	private Logentry entry;
	
	private int editLogentryRequestCode = 10;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.view_logentry_layout);
        setTitle("Log");
	}
	
	@Override
    public void onResume() {
        super.onResume();

        Bundle bundle = getIntent().getExtras();
        entry = DBUtil.db.getLogentryByNum(bundle.getInt("num"));
        
        ((TextView)findViewById(R.id.view_logentry_num)).setText(String.valueOf(entry.getNum()));
        ((TextView)findViewById(R.id.view_logentry_date)).setText(String.valueOf(entry.getDate().format("%Y-%m-%d")));
        ((TextView)findViewById(R.id.view_logentry_divesite)).setText(entry.getDiveSite().getName());
        ((TextView)findViewById(R.id.view_logentry_duration)).setText(String.valueOf(entry.getDuration()));
        ((TextView)findViewById(R.id.view_logentry_depth)).setText(String.valueOf(entry.getDepth()));
        ((TextView)findViewById(R.id.view_logentry_gasin)).setText(String.valueOf(entry.getGasIn()));
        ((TextView)findViewById(R.id.view_logentry_gasout)).setText(String.valueOf(entry.getGasOut()));
        ((TextView)findViewById(R.id.view_logentry_description)).setText(entry.getDescription());
    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == editLogentryRequestCode && resultCode == RESULT_OK) {
			getIntent().putExtra("num", data.getIntExtra("num", 0));
		}
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
            	editLogentryIntent.putExtra("id", entry.getId());
                startActivityForResult(editLogentryIntent, editLogentryRequestCode);
                return true;
                
            case R.id.logentry_delete_entry:
            	DBUtil.db.deleteLogentry(entry.getId());
            	finish();
            	return true;
            	
            default:
            	return super.onOptionsItemSelected(item);
        }
    }
}
