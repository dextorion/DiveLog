package com.divelog.activity.divesite;

import com.divelog.R;
import com.divelog.db.DBUtil;
import com.divelog.db.model.Divesite;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

import android.app.Activity;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class EditDivesiteActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
	
	private LocationClient locationClient = null;
	private LocationRequest locationRequest = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_divesite_layout);
        
        setTitle("Edit divesite");
        
        locationClient = new LocationClient(this, this, this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setNumUpdates(6);
        
	}
	
	@Override
	protected void onStart() {
        super.onStart();
        locationClient.connect();
    }
	
	@Override
	public void onResume() {
		super.onResume();
		Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("id")) {
        	Divesite divesite = DBUtil.db.getDivesite(extras.getInt("id"));
        	((EditText)findViewById(R.id.edit_divesite_name)).setText(divesite.getName());
    		((EditText)findViewById(R.id.edit_divesite_description)).setText(divesite.getDescription());
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
		EditText longitude = (EditText)findViewById(R.id.edit_divesite_longitude);
		EditText latitude = (EditText)findViewById(R.id.edit_divesite_latitude);
		
		if(!name.getText().toString().isEmpty()) {
			Bundle extras = getIntent().getExtras();
			DBUtil.db.saveDivesite(extras != null ? getIntent().getExtras().getInt("id") : null, name.getText().toString(), description.getText().toString(), Double.parseDouble(longitude.getText().toString()), Double.parseDouble(latitude.getText().toString()));
			Toast.makeText(this, "Divesite saved", Toast.LENGTH_SHORT).show();
		}
		
		locationClient.disconnect();
		finish();
	}

	public void onConnected(Bundle arg0) {
		locationClient.requestLocationUpdates(locationRequest, this);
		if(locationClient != null) {
			Location lastLocation = locationClient.getLastLocation();
			((EditText)findViewById(R.id.edit_divesite_longitude)).setText(String.valueOf(lastLocation.getLongitude()));
    		((EditText)findViewById(R.id.edit_divesite_latitude)).setText(String.valueOf(lastLocation.getLatitude()));
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
		((EditText)findViewById(R.id.edit_divesite_longitude)).setText(String.valueOf(lastLocation.getLongitude()));
		((EditText)findViewById(R.id.edit_divesite_latitude)).setText(String.valueOf(lastLocation.getLatitude()));
	}
}
