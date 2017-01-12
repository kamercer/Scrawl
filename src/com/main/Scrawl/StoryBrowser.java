package com.main.Scrawl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class StoryBrowser extends ActionBarActivity {

	private RecyclerView mRecyclerView;
	private RecyclerView.Adapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;

	String[] mGenreStrings;
	String chosenGenre = "Top Overall";
	char chosenTime = '1';

	Intent story;
	Context context = this;
	boolean refresh = false;

	private ArrayList<String> dataSet = new ArrayList<String>();
	private ArrayList<String> authorDataSet = new ArrayList<String>();
	private ArrayList<String> timeDataSet = new ArrayList<String>();
	private ArrayList<Integer> pointDataSet = new ArrayList<Integer>();
	private ArrayList<String> idDataSet = new ArrayList<String>();
	private ArrayList<String> commentDataSet = new ArrayList<String>();
	private ArrayList<String> titleDataSet = new ArrayList<String>();

	SwipeRefreshLayout mSwipeRefreshLayout;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prompt_selector);

		// If this is a phone, and not a tablet the screen orientation is locked
		if (getResources().getBoolean(R.bool.portrait_only)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				refresh();
			}

		});

		mGenreStrings = getResources().getStringArray(R.array.action_list);
		mAdapter = new StoryBrowserAdapter(dataSet, authorDataSet, timeDataSet,
				pointDataSet, titleDataSet);

		story = new Intent(context, DisplayBrowser.class);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				actionBar.getThemedContext(), R.array.action_list,
				android.R.layout.simple_spinner_item);

		adapter.setDropDownViewResource(R.layout.spinner_value_layout);

		OnNavigationListener mOnNavigationListener = new OnNavigationListener() {

			@Override
			public boolean onNavigationItemSelected(int position, long itemId) {
				chosenGenre = mGenreStrings[position];
				refresh();
				return true;
			}
		};

		actionBar.setListNavigationCallbacks(adapter, mOnNavigationListener);

		mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

		mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
				this, new RecyclerItemClickListener.OnItemClickListener() {
					@Override
					public void onItemClick(View view, int position) {
						story.putExtra("story", dataSet.get(position));
						story.putExtra("id", idDataSet.get(position));
						story.putExtra("commentId",
								commentDataSet.get(position));
						story.putExtra("title", titleDataSet.get(position));
						startActivity(story);
					}
				}));

		mRecyclerView.setHasFixedSize(true);

		// mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
		// DividerItemDecoration.VERTICAL_LIST));

		mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.setAdapter(mAdapter);

		AdView mAdView = (AdView) findViewById(R.id.adView1);

		// Create an ad request. Check logcat output for the hashed device ID to
		// get test ads on a physical device. e.g.
		// "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
		AdRequest adRequest = new AdRequest.Builder().build();

		// Start loading the ad in the background.
		mAdView.loadAd(adRequest);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.story_browser, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		if (item.getNumericShortcut() != '0') {
			chosenTime = item.getNumericShortcut();
			refresh();
		}

		return super.onOptionsItemSelected(item);
	}

	public void refresh() {
		if (refresh == true){
			return;
		}
		refresh = true;
		
		dataSet.clear();
		authorDataSet.clear();
		timeDataSet.clear();
		pointDataSet.clear();
		commentDataSet.clear();
		titleDataSet.clear();
		mAdapter.notifyDataSetChanged();

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Stories");
		if (!(chosenGenre.equals("All Genres"))) {
			query.whereEqualTo("genre", chosenGenre);
		}

		if (chosenTime != '1') {
			Date date = new Date();

			if (chosenTime == '2') {
				query.orderByDescending("upVote");
			} else if (chosenTime == '3') {
				date.setTime(Calendar.getInstance().getTimeInMillis()
						- (604800 * 1000));
				System.out.println(date.toString());
				query.whereGreaterThanOrEqualTo("createdAt", date);
				query.orderByDescending("upVote");
			} else if (chosenTime == '4') {
				long time = Calendar.getInstance().getTimeInMillis();
				time = time / 1000;
				time = time - 2592000;

				date.setTime(time * 1000);
				query.whereGreaterThanOrEqualTo("createdAt", date);
				query.orderByDescending("upVote");
			} else if (chosenTime == '5') {
				long time = Calendar.getInstance().getTimeInMillis();
				time = time / 1000;
				time = time - 31536000;

				date.setTime(time * 1000);
				query.whereGreaterThanOrEqualTo("createdAt", date);
				query.orderByDescending("upVote");
			}
		} else {
			query.orderByDescending("createdAt");

		}

		query.setLimit(20);

		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> storyList, ParseException e) {
				if (e == null) {

					for (int i = 0; i < storyList.size(); i++) {

						dataSet.add((String) storyList.get(i).get("story"));
						authorDataSet
								.add((String) storyList.get(i).get("user"));
						getTime(storyList.get(i).getCreatedAt());
						pointDataSet.add((Integer) storyList.get(i).get(
								"upVote"));
						idDataSet.add((String) storyList.get(i).getObjectId());
						commentDataSet.add((String) storyList.get(i).get(
								"commentId"));
						titleDataSet
								.add((String) storyList.get(i).get("Title"));
					}

					if (mSwipeRefreshLayout.isRefreshing() == true) {
						mSwipeRefreshLayout.setRefreshing(false);
					}

					mAdapter.notifyDataSetChanged();

				} else {

				}
			}
		});
		
		refresh = false;
	}

	public void getTime(Date date) {
		long result = (Calendar.getInstance().getTimeInMillis() / 1000)
				- (date.getTime() / 1000);

		if (result < 60) {
			if (result > 1) {
				timeDataSet
						.add(Integer.toString((int) result) + " seconds ago");
			} else {
				timeDataSet.add("1 second ago");
			}
			return;
		} else {
			result = result / 60;
		}

		if (result < 60) {
			if (result > 1) {
				timeDataSet
						.add(Integer.toString((int) result) + " minutes ago");
			} else {
				timeDataSet.add("1 minute ago");
			}
			return;
		} else {
			result = result / 60;
		}

		if (result < 24) {
			if (result > 1) {
				timeDataSet.add(Integer.toString((int) result) + " hours ago");
			} else {
				timeDataSet.add("1 hour ago");
			}
			return;
		} else {
			result = result / 24;
		}

		if (result < 30) {
			if (result > 1) {
				timeDataSet.add(Integer.toString((int) result) + " days ago");
			} else {
				timeDataSet.add("1 day ago");
			}
			return;
		} else {
			result = result / 30;
		}

		if (result < 365) {
			if (result > 1) {
				timeDataSet.add(Integer.toString((int) result) + " years ago");
			} else {
				timeDataSet.add("1 year ago");
			}
			return;
		} else {
			result = result / 365;
		}

	}

	public void createStory(View view) {
		Intent createPromptSelector = new Intent(this, WriteScreen.class);

		startActivity(createPromptSelector);
	}
}