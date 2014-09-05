package com.divelog.activity.divesite;

import java.util.ArrayList;
import java.util.List;

import com.divelog.R;
import com.divelog.db.DBUtil;
import com.divelog.db.model.Divesite;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DivesiteListActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.divesite_list_activity_layout);
        
        setTitle("Divesites");
        
        ListView divesiteListView = (ListView) findViewById(R.id.divesite_list);
         
        //=====[LIST HEADER]=====//
        LayoutInflater inflater = LayoutInflater.from(this);
        
        View headerView = inflater.inflate(R.layout.divesite_listitem_layout, null); 
        ((TextView)headerView.findViewById(R.id.divesite_listitem_id)).setText("#");
        ((TextView)headerView.findViewById(R.id.divesite_listitem_name)).setText("Name");
        
        divesiteListView.addHeaderView(headerView, null, true);
        divesiteListView.setHeaderDividersEnabled(true);
        //-----------------------//
        
        //==[LIST DATA ADAPTER]==//
    	List<Divesite> divesiteList = new ArrayList<Divesite>();
    	DivesiteAdapter divesiteAdapter = new DivesiteAdapter(this, divesiteList);
    	divesiteListView.setAdapter(divesiteAdapter);
    	//----------------------//
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	
    	ListView divesiteListView = (ListView) findViewById(R.id.divesite_list);

    	//====[LIST DATA]========//
    	DivesiteAdapter divesiteAdapter = (DivesiteAdapter)((HeaderViewListAdapter)divesiteListView.getAdapter()).getWrappedAdapter();
    	
    	divesiteAdapter.getDivesiteList().clear();
    	divesiteAdapter.getDivesiteList().addAll(DBUtil.db.getAllDivesites());
    	divesiteAdapter.notifyDataSetChanged();
        //-----------------------//
    	
    }

    public void viewDivesite(View view) {
    	ViewGroup parent = (ViewGroup)view.getParent();
    	CharSequence id = ((TextView)parent.findViewById(R.id.divesite_listitem_id)).getText();
    	boolean viewIsHeader = id.equals("#");
    	
    	if(!viewIsHeader) {
    		Bundle bundle = new Bundle();
    		bundle.putInt("id", Integer.parseInt(id.toString()));
    		
    		Intent viewIntent = new Intent();
    		viewIntent.putExtras(bundle);
    		viewIntent.setClass(this,ViewDivesiteActivity.class);
    		startActivity(viewIntent);
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
            	return false;
            	
            default:
            	return super.onOptionsItemSelected(item);
        }
    }
}
