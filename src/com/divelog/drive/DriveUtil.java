package com.divelog.drive;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.drive.Drive;

public class DriveUtil implements ConnectionCallbacks, OnConnectionFailedListener {

	public static DriveUtil drive = null;
	
	private GoogleApiClient driveApi;
	
	private boolean connected = false;
	
	public DriveUtil(Context context) {
		drive = this;
		driveApi = new GoogleApiClient.Builder(context).addApi(Drive.API).addScope(Drive.SCOPE_FILE).addScope(Drive.SCOPE_APPFOLDER).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
		driveApi.connect();
	}
	
	public void onConnectionFailed(ConnectionResult arg0) {
		Log.v("DriveUtil", "Could not connect to Drive.");
		connected = false;
	}

	public void onConnected(Bundle arg0) {
		connected = true;
	}

	public void onConnectionSuspended(int arg0) {
		Log.v("DriveUtil", "Connection suspended.");
		connected = false;
	}

}
