package com.main.Scrawl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.google.android.gms.plus.Plus;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class WriteScreen extends ActionBarActivity implements
		ConnectionCallbacks, OnConnectionFailedListener {

	GoogleApiClient mGoogleApiClient;
	private DriveId mSelectedFileDriveId;
	com.google.api.services.drive.Drive service;
	ParseObject storySaveObject;
	ParseQuery<ParseObject> updateStoryList;
	Toast successfulSave;
	Toast unsuccessfulSave;
	String storyText;
	Activity activity = this;
	String titleString;
	ArrayAdapter<CharSequence> adapter;
	String genre = "";
	ParseACL ACL;
	ParseUser currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_screen);

		// If this is a phone, and not a tablet the screen orientation is locked
		if (getResources().getBoolean(R.bool.portrait_only)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Drive.API)
				.addApi(Plus.API).addScope(Drive.SCOPE_FILE)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).build();

		Spinner spinner = (Spinner) findViewById(R.id.genreSpinner);

		adapter = new ArrayAdapter<CharSequence>(this,
				android.R.layout.simple_spinner_item);
		adapter.add("");
		adapter.add("Comedy");
		adapter.add("Fantasy");
		adapter.add("Historical Fiction");
		adapter.add("Horror");
		adapter.add("Mystery");
		adapter.add("Poetry");
		adapter.add("Realistic Fiction");
		adapter.add("Science Fiction");
		adapter.add("Thriller");
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				genre = (String) adapter.getItem(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}

		});
		spinner.setAdapter(adapter);
	}

	@Override
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {
		switch (requestCode) {
		case 5:
			if (resultCode == RESULT_OK) {
				mGoogleApiClient.connect();
			}
			break;
		case 6:
			if (resultCode == RESULT_OK) {
				mSelectedFileDriveId = (DriveId) data
						.getParcelableExtra(OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

				Collection<String> k = new ArrayList<String>();
				k.add(DriveScopes.DRIVE);

				GoogleAccountCredential credential = GoogleAccountCredential
						.usingOAuth2(this, k);
				credential.setSelectedAccountName(Plus.AccountApi
						.getAccountName(mGoogleApiClient));
				service = new com.google.api.services.drive.Drive.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), credential).build();

				AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

					@Override
					protected String doInBackground(Void... params) {
						try {
							File file = service.files()
									.get(mSelectedFileDriveId.getResourceId())
									.execute();

							System.out.println("Title: " + file.getTitle());
							System.out.println("Description: "
									+ file.getDescription());
							System.out.println("MIME type: "
									+ file.getMimeType());

							InputStream d = downloadFile(service, file);
							storyText = getStringFromInputStream(d);

							Spanned k = Html.fromHtml(storyText);

							storyText = k.toString().substring(
									k.toString().indexOf("\n") + 2);

							activity.runOnUiThread(new Runnable() {
								public void run() {
									activity.runOnUiThread(new Runnable() {
										public void run() {

											TextView s = (TextView) findViewById(R.id.SampleTextView);

											if (storyText.length() > 49) {
												if (storyText.contains("\n")) {
													if (storyText.indexOf("\n") > 49) {
														s.setText(storyText
																.substring(0,
																		49)
																+ "...");
													} else {
														s.setText(storyText
																.substring(
																		0,
																		storyText
																				.indexOf("\n"))
																+ "...");
													}
												}

											} else {
												Toast.makeText(
														activity,
														"Story has to be more than 50 characters",
														Toast.LENGTH_LONG)
														.show();
												storyText = null;
											}
										}
									});

								}
							});
						} catch (UserRecoverableAuthIOException e) {
							System.out.println("An error occured: " + e);
							startActivityForResult(e.getIntent(), 5);
						} catch (IOException e) {
							e.printStackTrace();
						}

						return null;
					}

				};

				task.execute(null, null, null);
			}
		default:
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	/**
	 * Download a file's content.
	 *
	 * @param service
	 *            Drive API service instance.
	 * @param file
	 *            Drive File instance.
	 * @return InputStream containing the file's content if successful,
	 *         {@code null} otherwise.
	 */
	private static InputStream downloadFile(
			com.google.api.services.drive.Drive service, File file) {
		// if (file.getDownloadUrl() != null && file.getDownloadUrl().length() >
		// 0) {
		try {
			HttpResponse resp = service
					.getRequestFactory()
					.buildGetRequest(
							new GenericUrl(file.getExportLinks().get(
									"text/html"))).execute();

			return resp.getContent();
		} catch (IOException e) {
			// An error occurred.
			e.printStackTrace();
			return null;
		}
	}

	public void submit(View view) {
		if (storyText == null) {
			Toast.makeText(this, "No story has been uploaded",
					Toast.LENGTH_LONG).show();
			return;
		}

		EditText title = (EditText) findViewById(R.id.titleEditText);
		titleString = title.getText().toString();
		if (titleString.length() == 0) {
			Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_LONG)
					.show();
			return;
		}

		if (genre.equals("")) {
			Toast.makeText(this, "You need to select a genre",
					Toast.LENGTH_LONG).show();
			return;
		}

		ACL = new ParseACL();
		ACL.setPublicWriteAccess(true);
		ACL.setPublicReadAccess(true);

		final ParseObject commentObject = new ParseObject("Comments");
		commentObject.add("Comments", "a");
		commentObject.getList("Comments").remove(0);
		commentObject.add("Users", "a");
		commentObject.getList("Users").remove(0);
		commentObject.setACL(ACL);
		commentObject.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				storySaveObject = new ParseObject("Stories");
				updateStoryList = ParseQuery.getQuery("ListOfStories");
				currentUser = ParseUser.getCurrentUser();

				storySaveObject.setACL(ACL);

				storySaveObject.put("user", currentUser.getUsername());
				storySaveObject.put("story", storyText);
				storySaveObject.put("upVote", 0);
				storySaveObject.put("genre", genre);
				storySaveObject.put("commentId", commentObject.getObjectId());
				storySaveObject.put("Title", titleString);
				storySaveObject.saveInBackground(new SaveCallback() {
					public void done(ParseException e) {
						if (e == null) {
							currentUser.add("stories", storySaveObject.getObjectId());
						} else {
						}
					}
				});
			}

		});
		finish();
	}

	@Override
	public void onConnected(Bundle result) {
		System.out.println("Connection passed");
	}

	@Override
	public void onConnectionSuspended(int arg0) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		System.out.println("Connection failed: " + result);
		if (result.hasResolution()) {
			try {
				result.startResolutionForResult(this, 5);
			} catch (IntentSender.SendIntentException e) {
				// Unable to resolve, message user appropriately
			}
		} else {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
					0).show();
		}
	}

	public void open(View view) {
		fileOpen();
	}

	// Import Button
	public void publish(View view) {
		if (!mGoogleApiClient.isConnected()) {
			Toast.makeText(this, "Google Drive is not Connected",
					Toast.LENGTH_LONG).show();
			return;
		}

		IntentSender intentSender = Drive.DriveApi
				.newOpenFileActivityBuilder()
				.setMimeType(
						new String[] { "application/vnd.google-apps.document" })
				.build(mGoogleApiClient);

		try {
			startIntentSenderForResult(intentSender, 6, null, 0, 0, 0);
		} catch (SendIntentException e) {
			System.out.println("publish failed: " + e);
		}
	}

	private void fileOpen() {
		Intent intent = getPackageManager().getLaunchIntentForPackage(
				"com.google.android.apps.docs.editors.docs");

		if (intent == null) {
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri
					.parse("market://details?id=com.google.android.apps.docs.editors.docs"));
		}

		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		try {
			startActivity(intent);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.write_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return super.onOptionsItemSelected(item);
	}

	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

}
