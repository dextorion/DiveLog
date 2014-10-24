package com.divelog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.divelog.activity.divesite.DivesiteListActivity;
import com.divelog.activity.logentry.LogentryListActivity;
import com.divelog.activity.sync.SyncActivity;
import com.divelog.db.DBUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi.MetadataBufferResult;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

public class MainActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener {
	
	private GoogleApiClient driveApi;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        setTitle("Dive Log");
        
        new DBUtil(this);
        
        driveApi = new GoogleApiClient.Builder(this).addApi(Drive.API).addScope(Drive.SCOPE_FILE).addScope(Drive.SCOPE_APPFOLDER).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        driveApi.connect();
	}
	
	public void startDivesiteListActivity(View view) {
		startActivity(new Intent(this, DivesiteListActivity.class));
	}
	
	public void startLogentryListActivity(View view) {
		startActivity(new Intent(this, LogentryListActivity.class));
	}
	
	@Override
	public void onBackPressed() {
		DBUtil.db.close();
		System.exit(0);
	}
	
	public void startSyncActivity(View view) {
		startActivity(new Intent(this, SyncActivity.class));
	}

	public void onConnectionFailed(ConnectionResult arg0) {
		Log.v("main", "something failed");
	}

	public void onConnected(Bundle connectionHint) {
	    Query query = new Query.Builder().addFilter(Filters.eq(SearchableField.TITLE, "DiveLogData")).build();
	    Drive.DriveApi.query(driveApi, query).setResultCallback(metadataCallback);
	}

	public void onConnectionSuspended(int arg0) {
		Log.v("main", "fell asleep");
	}
	
	final private ResultCallback<MetadataBufferResult> metadataCallback = new ResultCallback<MetadataBufferResult>() {

		public void onResult(MetadataBufferResult result) {
			MetadataBuffer metaDataBuffer = result.getMetadataBuffer();
	        for(Metadata metadata : metaDataBuffer) {
	        	Log.v("main", metadata.getDriveId().toString());
	        	Log.v("main", metadata.getTitle());
	        	break;
	        }
	        if (!result.getStatus().isSuccess()) {
	            Log.v("main", "some problem with metadata");
	            return;
	        }
		}
	};
}
