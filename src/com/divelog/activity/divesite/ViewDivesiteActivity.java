package com.divelog.activity.divesite;

import com.divelog.R;
import com.divelog.db.DBUtil;
import com.divelog.db.model.Divesite;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class ViewDivesiteActivity extends Activity {
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.view_divesite_layout);
        setTitle("Divesite");
	}
	
	@Override
    public void onResume() {
        super.onResume();

        Bundle bundle = getIntent().getExtras();
        Divesite diveSite = DBUtil.db.getDivesite(bundle.getInt("id"));
        
        ((TextView)findViewById(R.id.view_divesite_name)).setText(diveSite.getName());
        ((TextView)findViewById(R.id.view_divesite_description)).setText(diveSite.getDescription());
        
        MapFragment mapFragment = ((MapFragment)getFragmentManager().findFragmentById(R.id.view_divesite_map));
        if(diveSite.getLongitude() > 0) {
        	final GoogleMap map = mapFragment.getMap();
	        map.getUiSettings().setScrollGesturesEnabled(false);
	        map.getUiSettings().setMyLocationButtonEnabled(false);
	        map.getUiSettings().setIndoorLevelPickerEnabled(false);
	        map.getUiSettings().setTiltGesturesEnabled(false);
	        map.getUiSettings().setZoomGesturesEnabled(false);
	        map.addMarker(new MarkerOptions().position(new LatLng(diveSite.getLatitude(), diveSite.getLongitude())));
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(diveSite.getLatitude(), diveSite.getLongitude()), 10);
	        map.animateCamera(cameraUpdate);
        } else {
        	mapFragment.getView().setVisibility(View.INVISIBLE);
        }
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
            	finish();
            	return true;
            	
            default:
            	return super.onOptionsItemSelected(item);
        }
    }
}
