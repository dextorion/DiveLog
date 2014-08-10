package com.divelog.activity.divesite;

import com.divelog.R;
import com.divelog.db.DBUtil;
import com.divelog.db.model.Divesite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class ViewDivesiteActivity extends Activity {
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.view_divesite_layout);
        
        Bundle bundle = getIntent().getExtras();
        Divesite site = DBUtil.db.getDivesite(bundle.getInt("id"));
        
        ((TextView)findViewById(R.id.view_divesite_name)).setText(site.getName());
        ((TextView)findViewById(R.id.view_divesite_description)).setText(site.getDescription());
	}
	
	@Override
    public void onResume() {
        super.onResume();

        Bundle bundle = getIntent().getExtras();
        Divesite site = DBUtil.db.getDivesite(bundle.getInt("id"));
        
        ((TextView)findViewById(R.id.view_divesite_name)).setText(site.getName());
        ((TextView)findViewById(R.id.view_divesite_description)).setText(site.getDescription());
    }

	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.divesite_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
            case R.id.divesite_edit_entry:
            	Intent editDivesiteIntent = new Intent(this, EditDivesiteActivity.class);
                editDivesiteIntent.putExtras(getIntent().getExtras());
                startActivity(editDivesiteIntent);
                return true;
                
            case R.id.divesite_delete_entry:
            	Bundle bundle = getIntent().getExtras();
            	DBUtil.db.deleteDivesite(bundle.getInt("id"));
            	return true;
            	
            default:
            	return super.onOptionsItemSelected(item);
        }
    }
}
