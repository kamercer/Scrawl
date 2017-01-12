package com.main.Scrawl;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;

public class ParseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		ParseCrashReporting.enable(this);

		// Add your initialization code here
		Parse.initialize(this, "OFoMkBywuidtM9PEIJ9723M5ZbnYG9lCF4rGFBJ8",
				"HISIGdJXXqcmBJsotQqwVfIFf680eIuhygHMSYxw");

		// ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();

		// If you would like all objects to be private by default, remove this
		// line.
		defaultACL.setPublicReadAccess(true);

		ParseACL.setDefaultACL(defaultACL, true);

	}

}
