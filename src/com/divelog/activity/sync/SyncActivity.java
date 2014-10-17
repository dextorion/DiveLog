package com.divelog.activity.sync;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;

import com.divelog.R;
import com.divelog.db.DBUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder.DriveFileResult;
import com.google.android.gms.drive.MetadataChangeSet;

public class SyncActivity extends Activity implements ConnectionCallbacks,
		OnConnectionFailedListener {

	private static final int RESOLVE_CONNECTION_REQUEST_CODE = 1234;

	private GoogleApiClient driveApi;

	final private ResultCallback<DriveContentsResult> driveContentsCallback = new ResultCallback<DriveContentsResult>() {

		public void onResult(DriveContentsResult result) {
			if (!result.getStatus().isSuccess()) {
				// Toast.makeText(this, "Error creating content",
				// Toast.LENGTH_LONG);
				return;
			}

			@SuppressWarnings("unused")
			final DriveContents driveContents = result.getDriveContents();

			new Thread() {

				@Override
				public void run() {
					// File dbFile = new
					// File("/data/com.divelog/databases/divelog.db");

					try {
						// FileInputStream dbFile =
						// openFileInput("/data/com.divelog/databases/divelog.db");
						FileInputStream dbFile = new FileInputStream(new File("/data/data/com.divelog/databases/divelog.db"));
						String content = "";

						if (dbFile != null) {
							InputStreamReader dbFileReader = new InputStreamReader(dbFile);
							BufferedReader dbFileBuffer = new BufferedReader(dbFileReader);

							StringBuilder stringBuilder = new StringBuilder();

							while ((content = dbFileBuffer.readLine()) != null) {
								stringBuilder.append(content);
							}

							dbFileReader.close();
							content = stringBuilder.toString();
						}

						OutputStream outputStream = driveContents
								.getOutputStream();
						Writer writer = new OutputStreamWriter(outputStream);
						try {
							writer.write(content);
							writer.close();
						} catch (IOException e) {
							// Log or something
						}

						MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle("DiveLogData").setViewed(true).setMimeType("text/plain").build();

						// create a file on root folder
						Drive.DriveApi.getRootFolder(driveApi).createFile(driveApi, changeSet, driveContents).setResultCallback(fileCallback);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	};

	final private ResultCallback<DriveFileResult> fileCallback = new ResultCallback<DriveFileResult>() {

		public void onResult(DriveFileResult result) {
			if (!result.getStatus().isSuccess()) {
				// showMessage("Error while trying to create the file");
				return;
			}
			// showMessage("Created a file with content: " +
			// result.getDriveFile().getDriveId());
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sync_layout);
		setTitle("Sync");

		driveApi = new GoogleApiClient.Builder(this).addApi(Drive.API)
				.addScope(Drive.SCOPE_FILE).addScope(Drive.SCOPE_APPFOLDER)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).build();

		new DBUtil(this);
	}

	@Override
	public void onStart() {
		super.onStart();
		driveApi.connect();
	}

	@Override
	public void onStop() {
		driveApi.disconnect();
		super.onStop();
	}

	public void syncToDrive(View view) {
		Drive.DriveApi.newDriveContents(driveApi).setResultCallback(
				driveContentsCallback);
	}

	public void onConnected(Bundle connectionHint) {
		// enable button
	}

	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {
		switch (requestCode) {
		case RESOLVE_CONNECTION_REQUEST_CODE:
			if (!driveApi.isConnecting() && !driveApi.isConnected()) {
				driveApi.connect();
			}
			break;
		}
	}

	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (connectionResult.hasResolution()) {
			try {
				connectionResult.startResolutionForResult(this,
						RESOLVE_CONNECTION_REQUEST_CODE);
			} catch (IntentSender.SendIntentException e) {
				// Unable to resolve, message user appropriately
			}
		} else {
			GooglePlayServicesUtil.getErrorDialog(
					connectionResult.getErrorCode(), this, 0).show();
		}
	}
}
