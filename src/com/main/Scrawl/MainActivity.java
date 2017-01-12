package com.main.Scrawl;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends ActionBarActivity {

	Intent storyBrowser;
	EditText username;
	EditText password;
	Activity mainActivity = this;
	boolean newBit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// If this is a phone, and not a tablet the screen orientation is locked
		if (getResources().getBoolean(R.bool.portrait_only)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		ParseAnalytics.trackAppOpened(getIntent());

		ParseUser currentUser = ParseUser.getCurrentUser();

		if (currentUser != null) {
			storyBrowser = new Intent(this, StoryBrowser.class);

			startActivity(storyBrowser);
		}
	}

	public void onClick(View view) {
		if (newBit == false) {
			setContentView(R.layout.new_account_screen);
			newBit = true;
			return;
		}

		username = (EditText) findViewById(R.id.usernameEditText);
		password = (EditText) findViewById(R.id.passwordEditText);
		EditText reEnterPassword = (EditText) findViewById(R.id.ReenterpasswordEditText);

		if (username.getText().toString().length() == 0) {
			Toast.makeText(this, "Username is empty", Toast.LENGTH_LONG).show();
			return;
		}
		
		if (username.getText().toString().length() > 20) {
			Toast.makeText(this, "Username is more than 20 characters", Toast.LENGTH_LONG).show();
			return;
		}
		
		if (password.getText().toString().length() == 0) {
			Toast.makeText(this, "Password is empty", Toast.LENGTH_LONG).show();
			return;
		}
		if (reEnterPassword.getText().toString().length() == 0) {
			Toast.makeText(this, "Re-Enter Password is empty",
					Toast.LENGTH_LONG).show();
			return;
		}

		if (!reEnterPassword.getText().toString()
				.equals(password.getText().toString())) {
			Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG)
					.show();
			return;
		}

		ParseUser user = new ParseUser();
		user.setUsername(username.getText().toString());
		user.setPassword(password.getText().toString());

		user.add("recommendations", "a");
		user.getList("recommendations").remove(0);
		
		user.add("stories", "a");
		user.getList("stories").remove(0);

		storyBrowser = new Intent(this, StoryBrowser.class);

		user.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {
				if (e == null) {
					Log.d("MyApp", "Login successful");
					startActivity(storyBrowser);
				} else {
					Log.d("MyApp", "Login unsuccessful");
					if (e.getCode() == 202) {
						Toast.makeText(mainActivity, "Username is Taken",
								Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(mainActivity, "Unable to Sign Up",
								Toast.LENGTH_LONG).show();
					}
				}
			}
		});
	}

	public void login(View view) {
		username = (EditText) findViewById(R.id.usernameEditText);
		password = (EditText) findViewById(R.id.passwordEditText);

		if (username.getText().toString().length() == 0) {
			Toast.makeText(this, "Username is empty", Toast.LENGTH_LONG).show();
			return;
		}
		if (password.getText().toString().length() == 0) {
			Toast.makeText(this, "Password is empty", Toast.LENGTH_LONG).show();
			return;
		}

		storyBrowser = new Intent(this, StoryBrowser.class);

		ParseUser.logInInBackground(username.getText().toString(), password
				.getText().toString(), new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					startActivity(storyBrowser);
				} else {
					Toast.makeText(mainActivity, "Login Failed",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	public void onBackPressed() {
		if (newBit == true) {
			setContentView(R.layout.activity_main);
			newBit = false;
		} else {
			super.onBackPressed();
		}
	}
}