package com.divelog.activity.logentry;

import java.util.ArrayList;
import java.util.List;

import com.divelog.R;
import com.divelog.db.DBUtil;
import com.divelog.db.model.Logentry;

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

public class LogentryListActivity extends Activity {

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logentry_list_activity_layout);

        ListView logentryListView = (ListView) findViewById(R.id.logentry_list);
        
        //=====[HEADER ITEM]===============================================================
        LayoutInflater inflater = LayoutInflater.from(this);
        
        View headerView = inflater.inflate(R.layout.logentry_listitem_layout, null); 
        ((TextView)headerView.findViewById(R.id.logentry_listitem_id)).setText("#");
        ((TextView)headerView.findViewById(R.id.logentry_listitem_date)).setText("Date");
        ((TextView)headerView.findViewById(R.id.logentry_listitem_dive_site)).setText("Dive site");
        
        logentryListView.addHeaderView(headerView, null, true);
        logentryListView.setHeaderDividersEnabled(true);
        //=================================================================================


        List<Logentry> logEntryList = new ArrayList<Logentry>();
        LogentryAdapter logEntryAdapter = new LogentryAdapter(this, logEntryList);
        logentryListView.setAdapter(logEntryAdapter);
        
    }

    @Override
    public void onResume() {
        super.onResume();

        ListView logentryListView = (ListView) findViewById(R.id.logentry_list);

        //====[LIST DATA]========//
        LogentryAdapter logentryAdapter = (LogentryAdapter)((HeaderViewListAdapter)logentryListView.getAdapter()).getWrappedAdapter();

        logentryAdapter.getLogentryList().clear();
        logentryAdapter.getLogentryList().addAll(DBUtil.db.getAllLogentries());
        logentryAdapter.notifyDataSetChanged();
        //-----------------------//

    }

    public void viewLogentry(View view) {
        ViewGroup parent = (ViewGroup)view.getParent();
        CharSequence id = ((TextView)parent.findViewById(R.id.logentry_listitem_id)).getText();
        boolean viewIsHeader = id.equals("#");

        if(!viewIsHeader) {
            Bundle bundle = new Bundle();
            bundle.putInt("id", Integer.parseInt(id.toString()));

            Intent viewIntent = new Intent();
            viewIntent.putExtras(bundle);
            viewIntent.setClass(this,ViewLogentryActivity.class);
            startActivity(viewIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logentry_list_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
            case R.id.logentry_list_add_entry:
                startActivity(new Intent(this, EditLogentryActivity.class));
                return true;
            
            case R.id.logentry_list_settings:
            	//startActivity(new Intent(this, LogentrySettings.class));
            	return false;
            	
            default:
            return super.onOptionsItemSelected(item);
        }
    }
}
