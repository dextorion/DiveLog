package com.divelog;

import java.util.ArrayList;
import java.util.List;

import com.divelog.model.Logentry;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class DivesiteListActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.divesite_list_activity_layout);
        
        ListView logEntryListView = (ListView) findViewById(R.id.divesite_list);
        
        //=====[HEADER ITEM]===============================================================
        LayoutInflater inflater = LayoutInflater.from(this);
        
        View headerView = inflater.inflate(R.layout.divesite_listitem_layout, null); 
        ((TextView)headerView.findViewById(R.id.divesite_listitem_id)).setText("#");
        ((TextView)headerView.findViewById(R.id.divesite_listitem_date)).setText("Date");
        ((TextView)headerView.findViewById(R.id.divesite_listitem_dive_site)).setText("Dive site");
        
        logEntryListView.addHeaderView(headerView, null, true);
        logEntryListView.setHeaderDividersEnabled(true);
        //=================================================================================
        
        //=====[TEST DATA]=================================================================
        List<Logentry> logEntryList = new ArrayList<Logentry>();
        logEntryList.add(new Logentry(1, new Time(), "Båtmans brygga"));
        logEntryList.add(new Logentry(2, new Time(), "Svartfots Vraket"));
        logEntryList.add(new Logentry(3, new Time(), "Falken"));
        
        LogentryAdapter logEntryAdapter = new LogentryAdapter(this, logEntryList);
        //=================================================================================
        
        logEntryListView.setAdapter(logEntryAdapter);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.divesite_list_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
            case R.id.divesite_list_add_entry:
                startActivity(new Intent(this, EditDivesiteActivity.class));
                return true;
            
            case R.id.divesite_list_settings:
            	//startActivity(new Intent(this, DivesiteSettingsActivity.class));
            	return true;
            	
            default:
            return super.onOptionsItemSelected(item);
        }
    }
}
