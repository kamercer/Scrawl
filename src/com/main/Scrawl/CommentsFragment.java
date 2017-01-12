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

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Simple Fragment used to display some meaningful content for each page in the
 * sample's {@link android.support.v4.view.ViewPager}.
 */
public class CommentsFragment extends Fragment {

	private static final String KEY_TITLE = "title";
	private static final String KEY_INDICATOR_COLOR = "indicator_color";
	private static final String KEY_DIVIDER_COLOR = "divider_color";
	private static String commentId = "";
	EditText commentEditText;
	ArrayList<String> commentList = new ArrayList<String>();
	ArrayList<String> authorDataSet = new ArrayList<String>();
	RecyclerView.Adapter mAdapter;
	Activity activity;

	AdView mAdView;

	/**
	 * @return a new instance of {@link ContentFragment}, adding the parameters
	 *         into a bundle and setting them as arguments.
	 */
	public static CommentsFragment newInstance(CharSequence title,
			int indicatorColor, int dividerColor, String commentId) {
		Bundle bundle = new Bundle();
		bundle.putCharSequence(KEY_TITLE, title);
		bundle.putInt(KEY_INDICATOR_COLOR, indicatorColor);
		bundle.putInt(KEY_DIVIDER_COLOR, dividerColor);
		bundle.putString("commentId", commentId);

		CommentsFragment fragment = new CommentsFragment();
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.comments_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Bundle args = getArguments();
		if (args != null) {
			commentId = args.getString("commentId");
		}

		commentEditText = (EditText) view.findViewById(R.id.commentInput);

		commentEditText
				.setOnEditorActionListener(new EditText.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {

							if (commentEditText.getText().toString().length() > 240) {
								Toast.makeText(activity, "Comment is more than 240 characters",
										Toast.LENGTH_LONG).show();
								return false;
							}

							ParseQuery<ParseObject> query = ParseQuery
									.getQuery("Comments");
							query.getInBackground(commentId,
									new GetCallback<ParseObject>() {

										@Override
										public void done(
												ParseObject commentList,
												ParseException e) {
											ParseUser user = ParseUser
													.getCurrentUser();
											if (e == null) {
												commentList.getList("Comments")
														.add(commentEditText
																.getText()
																.toString());
												commentList
														.getList("Users")
														.add(user.getUsername());

												commentList.saveInBackground();
											}
											hideSoftKeyboard();
											commentEditText.setText("");
											refresh();
										}
									});

							return true;
						}
						return false;
					}
				});

		RecyclerView.LayoutManager mLayoutManager;
		RecyclerView recyclerView = (RecyclerView) view
				.findViewById(R.id.my_recycler_view);
		recyclerView.setHasFixedSize(true);

		mLayoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(mLayoutManager);
		mAdapter = new CommentAdapter(commentList, authorDataSet);
		recyclerView.setAdapter(mAdapter);

		refresh();
	}

	public void refresh() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Comments");
		commentList.clear();
		authorDataSet.clear();

		query.getInBackground(commentId, new GetCallback<ParseObject>() {

			@Override
			public void done(ParseObject object, ParseException e) {
				if (object.getList("Comments") != null) {
					for (int i = 0; i < object.getList("Comments").size(); i++) {
						commentList.add((String) object.getList("Comments")
								.get(i));
						authorDataSet.add((String) object.getList("Users").get(
								i));
					}
				}
				mAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		activity = getActivity();
	}

	public void hideSoftKeyboard() {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), 0);
	}
}