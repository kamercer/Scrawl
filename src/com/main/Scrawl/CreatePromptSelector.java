package com.main.Scrawl;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseObject;
import com.parse.ParseQuery;

public class CreatePromptSelector extends ActionBarActivity {

	private RecyclerView mRecyclerView;
	private RecyclerView.Adapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;

	private ArrayList<String> dataSet = new ArrayList<String>();
	boolean inPromptsList = false;
	String selectedGenre;

	ParseQuery<ParseObject> query;

	Activity thisActivity = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_selector);

		setUpGenres();

		mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

		mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
				this, new RecyclerItemClickListener.OnItemClickListener() {
					@Override
					public void onItemClick(View view, int position) {
						Intent writeScreen = new Intent(thisActivity,
								WriteScreen.class);

						writeScreen.putExtra("genre", dataSet.get(position));

						startActivity(writeScreen);
						finish();
					}
				}));

		mRecyclerView.setHasFixedSize(true);

		mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
				DividerItemDecoration.VERTICAL_LIST));

		mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);

		mAdapter = new CreateSelectorAdapter(dataSet);
		mRecyclerView.setAdapter(mAdapter);

	}

	public void setUpGenres() {
		dataSet.add("Comedy");
		dataSet.add("Fantasy");
		dataSet.add("Historical Fiction");
		dataSet.add("Horror");
		dataSet.add("Mystery");
		dataSet.add("Realistic Fiction");
		dataSet.add("Science Fiction");
		dataSet.add("Thriller");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.prompt_selector, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return super.onOptionsItemSelected(item);
	}

}