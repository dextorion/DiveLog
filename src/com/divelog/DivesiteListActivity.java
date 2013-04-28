package com.divelog;

import java.util.ArrayList;
import java.util.List;

import com.divelog.db.DataSource;
import com.divelog.model.Divesite;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ListView;
import android.widget.TextView;

public class DivesiteListActivity extends Activity {

	DataSource dataSource;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.divesite_list_activity_layout);
        dataSource = new DataSource(this);
        dataSource.open();
        
        ListView divesiteListView = (ListView) findViewById(R.id.divesite_list);
        
        
        //=====[LIST HEADER]=====//
        LayoutInflater inflater = LayoutInflater.from(this);
        
        View headerView = inflater.inflate(R.layout.divesite_listitem_layout, null); 
        ((TextView)headerView.findViewById(R.id.divesite_listitem_id)).setText("#");
        ((TextView)headerView.findViewById(R.id.divesite_listitem_name)).setText("Name");
        
        divesiteListView.addHeaderView(headerView, null, true);
        divesiteListView.setHeaderDividersEnabled(true);
        //-----------------------//
        
        //=====[TEST DATA]=======//
        List<Divesite> divesiteList = new ArrayList<Divesite>();
        divesiteList.add(new Divesite(1, "Båtmans brygga", ""));
        divesiteList.add(new Divesite(1, "M/S Harm", ""));
        //-----------------------//
        
        //====[LIST DATA]========//
        divesiteList.addAll(dataSource.getAllDivesites());
        
        DivesiteAdapter divesiteAdapter = new DivesiteAdapter(this, divesiteList);
        divesiteListView.setAdapter(divesiteAdapter);
        //-----------------------//
        
    }

    public void editDivesite(View view) {
    	ViewGroup parent = (ViewGroup)view.getParent();
    	boolean viewIsHeader = ((TextView)parent.findViewById(R.id.divesite_listitem_id)).getText().equals("#");
    	if(!viewIsHeader) {
    		startActivity(new Intent(this, EditDivesiteActivity.class));
    	}
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
