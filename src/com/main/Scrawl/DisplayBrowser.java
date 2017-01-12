package com.main.Scrawl;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

public class DisplayBrowser extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_holder);

		// If this is a phone, and not a tablet the screen orientation is locked
		if (getResources().getBoolean(R.bool.portrait_only)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		String storyId = getIntent().getStringExtra("id");
		String story = getIntent().getStringExtra("story");
		String commentId = getIntent().getStringExtra("commentId");
		
		getSupportActionBar().setTitle(getIntent().getStringExtra("title"));

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		SlidingTabsColorsFragment fragment = new SlidingTabsColorsFragment(
				story, storyId, commentId);
		transaction.replace(R.id.sample_content_fragment, fragment);
		transaction.commit();
	}

}
