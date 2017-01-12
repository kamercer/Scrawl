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

import com.main.Scrawl.SlidingTabLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic sample which shows how to use
 * {@link com.example.android.common.view.SlidingTabLayout} to display a custom
 * {@link ViewPager} title strip which gives continuous feedback to the user
 * when scrolling.
 */
public class SlidingTabsColorsFragment extends Fragment {

	String mStory;
	String mPrompt;
	String commentId;

	public SlidingTabsColorsFragment(String story, String prompt, String cId) {
		mStory = story;
		mPrompt = prompt;
		commentId = cId;
	}
	
	public SlidingTabsColorsFragment(){
		
	}

	/**
	 * This class represents a tab to be displayed by {@link ViewPager} and it's
	 * associated {@link SlidingTabLayout}.
	 */
	static class StoryPagerItem {
		private final CharSequence mTitle;
		private final int mIndicatorColor;
		private final int mDividerColor;
		private String Story;
		private String Prompt;

		StoryPagerItem(CharSequence title, int indicatorColor,
				int dividerColor, String story, String prompt) {
			mTitle = title;
			mIndicatorColor = indicatorColor;
			mDividerColor = dividerColor;
			Story = story;
			Prompt = prompt;
		}

		/**
		 * @return A new {@link Fragment} to be displayed by a {@link ViewPager}
		 */
		Fragment createFragment() {
			return StoryFragment.newInstance(mTitle, mIndicatorColor,
					mDividerColor, Story, Prompt);
		}

		/**
		 * @return the title which represents this tab. In this sample this is
		 *         used directly by
		 *         {@link android.support.v4.view.PagerAdapter#getPageTitle(int)}
		 */
		CharSequence getTitle() {
			return mTitle;
		}

		/**
		 * @return the color to be used for indicator on the
		 *         {@link SlidingTabLayout}
		 */
		int getIndicatorColor() {
			return mIndicatorColor;
		}

		/**
		 * @return the color to be used for right divider on the
		 *         {@link SlidingTabLayout}
		 */
		int getDividerColor() {
			return mDividerColor;
		}
	}

	static class CommentsPagerItem {
		private final CharSequence mTitle;
		private final int mIndicatorColor;
		private final int mDividerColor;
		private final String commentId;

		CommentsPagerItem(CharSequence title, int indicatorColor,
				int dividerColor, String cId) {
			mTitle = title;
			mIndicatorColor = indicatorColor;
			mDividerColor = dividerColor;
			commentId = cId;
		}

		/**
		 * @return A new {@link Fragment} to be displayed by a {@link ViewPager}
		 */
		Fragment createFragment() {
			return CommentsFragment.newInstance(mTitle, mIndicatorColor,
					mDividerColor, commentId);
		}

		/**
		 * @return the title which represents this tab. In this sample this is
		 *         used directly by
		 *         {@link android.support.v4.view.PagerAdapter#getPageTitle(int)}
		 */
		CharSequence getTitle() {
			return mTitle;
		}

		/**
		 * @return the color to be used for indicator on the
		 *         {@link SlidingTabLayout}
		 */
		int getIndicatorColor() {
			return mIndicatorColor;
		}

		/**
		 * @return the color to be used for right divider on the
		 *         {@link SlidingTabLayout}
		 */
		int getDividerColor() {
			return mDividerColor;
		}
	}

	static final String LOG_TAG = "SlidingTabsColorsFragment";

	/**
	 * A custom {@link ViewPager} title strip which looks much like Tabs present
	 * in Android v4.0 and above, but is designed to give continuous feedback to
	 * the user when scrolling.
	 */
	private SlidingTabLayout mSlidingTabLayout;

	/**
	 * A {@link ViewPager} which will be used in conjunction with the
	 * {@link SlidingTabLayout} above.
	 */
	private ViewPager mViewPager;

	/**
	 * List of {@link SamplePagerItem} which represent this sample's tabs.
	 */

	private List<StoryPagerItem> mTabs = new ArrayList<StoryPagerItem>();

	private List<CommentsPagerItem> commentTab = new ArrayList<CommentsPagerItem>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// BEGIN_INCLUDE (populate_tabs)
		/**
		 * Populate our tab list with tabs. Each item contains a title,
		 * indicator color and divider color, which are used by
		 * {@link SlidingTabLayout}.
		 */
		mTabs.add(new StoryPagerItem("Story", // Title
				getResources().getColor(R.color.Accent), // Indicator color
				Color.GRAY, // Divider color
				mStory, mPrompt));

		commentTab.add(new CommentsPagerItem("Comments", // Title
				getResources().getColor(R.color.Accent), // Indicator color
				Color.GRAY, // Divider color
				commentId));
		// END_INCLUDE (populate_tabs)
	}

	/**
	 * Inflates the {@link View} which will be displayed by this
	 * {@link Fragment}, from the app's resources.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_sample, container, false);
	}

	// BEGIN_INCLUDE (fragment_onviewcreated)
	/**
	 * This is called after the
	 * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} has finished.
	 * Here we can pick out the {@link View}s we need to configure from the
	 * content view.
	 *
	 * We set the {@link ViewPager}'s adapter to be an instance of
	 * {@link SampleFragmentPagerAdapter}. The {@link SlidingTabLayout} is then
	 * given the {@link ViewPager} so that it can populate itself.
	 *
	 * @param view
	 *            View created in
	 *            {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// BEGIN_INCLUDE (setup_viewpager)
		// Get the ViewPager and set it's PagerAdapter so that it can display
		// items
		mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
		mViewPager.setAdapter(new SampleFragmentPagerAdapter(
				getChildFragmentManager()));
		// END_INCLUDE (setup_viewpager)

		// BEGIN_INCLUDE (setup_slidingtablayout)
		// Give the SlidingTabLayout the ViewPager, this must be done AFTER the
		// ViewPager has had
		// it's PagerAdapter set.
		mSlidingTabLayout = (SlidingTabLayout) view
				.findViewById(R.id.sliding_tabs);
		mSlidingTabLayout.setViewPager(mViewPager);

		// BEGIN_INCLUDE (tab_colorizer)
		// Set a TabColorizer to customize the indicator and divider colors.
		// Here we just retrieve
		// the tab at the position, and return it's set color
		mSlidingTabLayout
				.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

					@Override
					public int getIndicatorColor(int position) {
						if (position == 0) {
							return mTabs.get(position).getIndicatorColor();
						}else{
							return commentTab.get(0).getIndicatorColor();
						}
					}

					@Override
					public int getDividerColor(int position) {
						if (position == 0) {
							return mTabs.get(position).getDividerColor();
						}else{
							return commentTab.get(0).getDividerColor();
						}
					}

				});
		// END_INCLUDE (tab_colorizer)
		// END_INCLUDE (setup_slidingtablayout)
	}

	// END_INCLUDE (fragment_onviewcreated)

	/**
	 * The {@link FragmentPagerAdapter} used to display pages in this sample.
	 * The individual pages are instances of {@link ContentFragment} which just
	 * display three lines of text. Each page is created by the relevant
	 * {@link SamplePagerItem} for the requested position.
	 * <p>
	 * The important section of this class is the {@link #getPageTitle(int)}
	 * method which controls what is displayed in the {@link SlidingTabLayout}.
	 */
	class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

		SampleFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		/**
		 * Return the {@link android.support.v4.app.Fragment} to be displayed at
		 * {@code position}.
		 * <p>
		 * Here we return the value returned from
		 * {@link SamplePagerItem#createFragment()}.
		 */
		@Override
		public Fragment getItem(int i) {
			if (i == 0){
				return mTabs.get(i).createFragment();
			}else{
				return commentTab.get(0).createFragment();
			}
		}

		@Override
		public int getCount() {
			return 2;
		}

		// BEGIN_INCLUDE (pageradapter_getpagetitle)
		/**
		 * Return the title of the item at {@code position}. This is important
		 * as what this method returns is what is displayed in the
		 * {@link SlidingTabLayout}.
		 * <p>
		 * Here we return the value returned from
		 * {@link SamplePagerItem#getTitle()}.
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			if (position == 0){
				return mTabs.get(position).getTitle();
			}else{
				return commentTab.get(0).getTitle();
			}
			
		}
		// END_INCLUDE (pageradapter_getpagetitle)

	}

}