/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.main.Scrawl;

import java.util.ArrayList;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Simple Fragment used to display some meaningful content for each page in the sample's
 * {@link android.support.v4.view.ViewPager}.
 */
public class StoryFragment extends Fragment {

    private static final String KEY_TITLE = "title";
    private static final String KEY_INDICATOR_COLOR = "indicator_color";
    private static final String KEY_DIVIDER_COLOR = "divider_color";
    private static String mStory = "";
    private static String mStoryId = "";
    
    AdView mAdView;
    
    boolean buttonBit = true;
    ArrayList<String> k;
	ParseUser user;
	
	Button button;

    /**
     * @return a new instance of {@link ContentFragment}, adding the parameters into a bundle and
     * setting them as arguments.
     */
    public static StoryFragment newInstance(CharSequence title, int indicatorColor,
            int dividerColor, String story, String id) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(KEY_TITLE, title);
        bundle.putInt(KEY_INDICATOR_COLOR, indicatorColor);
        bundle.putInt(KEY_DIVIDER_COLOR, dividerColor);
        bundle.putString("Story", story);
        bundle.putString("id", id);

        StoryFragment fragment = new StoryFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_display_browser, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        button = (Button) view.findViewById(R.id.Recommend);
        
        button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if (buttonBit == true) {
					buttonBit = false;

					ParseQuery<ParseObject> up = ParseQuery.getQuery("Stories");
					up.getInBackground(mStoryId, new GetCallback<ParseObject>() {
						public void done(ParseObject story, ParseException e) {
							if (e == null) {

								story.increment("upVote");

								story.saveInBackground();

								user.add("recommendations", mStoryId);
								user.saveInBackground();

								button.setSelected(true);
								button.setEnabled(false);
							} else {
								buttonBit = true;
							}
						}
					});
				}
			}
        	
        });
        
        TextView storyView = (TextView) view.findViewById(R.id.storyView);
		
		storyView.setMovementMethod(new ScrollingMovementMethod());
		
		Bundle args = getArguments();
        if (args != null) {
        	mStory = args.getString("Story");
        	mStoryId = args.getString("id");
        	
        	storyView.setText(mStory);
        }
		
		user = ParseUser.getCurrentUser();
		k = (ArrayList<String>) user.get("recommendations");

		if (k.contains(mStoryId)) {
			button.setSelected(true);
			button.setEnabled(false);
		}
        
        mAdView = (AdView) view.findViewById(R.id.adView);

		// Create an ad request. Check logcat output for the hashed device ID to
		// get test ads on a physical device. e.g.
		// "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
		AdRequest adRequest = new AdRequest.Builder().build();

		// Start loading the ad in the background.
		mAdView.loadAd(adRequest);
    }
}
