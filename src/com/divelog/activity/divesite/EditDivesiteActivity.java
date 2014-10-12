package com.divelog.activity.divesite;

import com.divelog.R;
import com.divelog.db.DBUtil;
import com.divelog.db.model.Divesite;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditDivesiteActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
	
	private LocationClient locationClient = null;
	private LocationRequest locationRequest = null;
	
	private double longitude;
	private double latitude;
	
	private float accuracy = 999;
	
	private int siteId = 0;
	
	private GoogleMap map;
	
	Divesite divesite = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_divesite_layout);
        
        setTitle("Edit divesite");
        
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("id")) {
        	siteId = extras.getInt("id");
        }
        
        if(siteId > 0) {
        	divesite = DBUtil.db.getDivesite(siteId);
        	((EditText)findViewById(R.id.edit_divesite_name)).setText(divesite.getName());
    		((EditText)findViewById(R.id.edit_divesite_description)).setText(divesite.getDescription());
        }
        
        map = ((MapFragment)getFragmentManager().findFragmentById(R.id.edit_divesite_map)).getMap();
        map.getUiSettings().setScrollGesturesEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setIndoorLevelPickerEnabled(false);
        map.getUiSettings().setTiltGesturesEnabled(false);
        map.setMyLocationEnabled(true);
        
        locationClient = new LocationClient(this, this, this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setNumUpdates(60);
	}
	
	@Override
	protected void onStart() {
        super.onStart();
        if(siteId == 0 || divesite.getLatitude() == 0 || divesite.getLatitude() == null) {
        	locationClient.connect();
        }
    }
	
	@Override
	public void onStop() { 
		locationClient.disconnect();
		super.onStop();
	}
	
	@Override
	public void onBackPressed() {
		EditText name = (EditText)findViewById(R.id.edit_divesite_name);
		EditText description = (EditText)findViewById(R.id.edit_divesite_description);
		
		if(!name.getText().toString().isEmpty()) {
			DBUtil.db.saveDivesite(siteId > 0 ? siteId : null, name.getText().toString(), description.getText().toString(), longitude, latitude);
		}
		
		locationClient.removeLocationUpdates(this);
		locationClient.disconnect();
		finish();
	}

	public void onConnected(Bundle arg0) {
		locationClient.requestLocationUpdates(locationRequest, this);
		Location lastLocation = locationClient.getLastLocation();
		
		if(lastLocation.getAccuracy() < accuracy) {
			updateLocation(lastLocation);
		}
    		
	}

	public void onDisconnected() {
		Toast.makeText(this, "Disconnected. Please re-connect.",Toast.LENGTH_SHORT).show();
	}

	public void onConnectionFailed(ConnectionResult connectionResult) {
		
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this,101);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "GPS Error", Toast.LENGTH_SHORT).show();
        }
	}

	public void onLocationChanged(Location location) {
		Location lastLocation = locationClient.getLastLocation();
		
		if(lastLocation.getAccuracy() < accuracy) {
			updateLocation(lastLocation);
		}
	}
	
	private void updateLocation(Location lastLocation) {
		longitude = lastLocation.getLongitude();
		latitude = lastLocation.getLatitude();
		accuracy = lastLocation.getAccuracy();
		
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15);
        map.animateCamera(cameraUpdate);
		
		TextView accuracyView = ((TextView)findViewById(R.id.edit_divesite_accuracy));
		accuracyView.setText("Fetching location... accuracy: " + String.valueOf((int)accuracy) + " meters");
		if(accuracy < 6 ) {
			accuracyView.setTextColor(Color.rgb(0, 255, 0));
			accuracyView.setText("Location");
		} else if (accuracy < 20){
			accuracyView.setTextColor(Color.rgb(240, 240, 0));
		} else {
			accuracyView.setTextColor(Color.rgb(255, 0, 0));
		}
	}
}
