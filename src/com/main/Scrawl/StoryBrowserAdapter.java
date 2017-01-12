package com.main.Scrawl;

import java.util.ArrayList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StoryBrowserAdapter extends
		RecyclerView.Adapter<StoryBrowserAdapter.ListItemViewHolder> {

	private ArrayList<String> mDataSet;
	private ArrayList<Integer> secondaryDataSet;
	private ArrayList<String> mAuthorDataSet;
	private ArrayList<String> mTimeDataSet;
	private ArrayList<Integer> mPointDataSet;
	private ArrayList<String> mTitleDataSet;

	StoryBrowserAdapter(ArrayList<String> dataSet,
			ArrayList<String> authorDataSet, ArrayList<String> timeDataSet,
			ArrayList<Integer> pointDataSet, ArrayList<String> titleDataSet) {
		if (dataSet == null) {
			throw new IllegalArgumentException("modelData must not be null");
		}
		mDataSet = dataSet;
		mAuthorDataSet = authorDataSet;
		mTimeDataSet = timeDataSet;
		mPointDataSet = pointDataSet;
		mTitleDataSet = titleDataSet;
	}

	@Override
	public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup,
			int viewType) {
		View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
				R.layout.story_browser_text_view, viewGroup, false);

		return new ListItemViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(ListItemViewHolder viewHolder, int position) {
		String title = mTitleDataSet.get(position);

		viewHolder.label.setText(title);
		viewHolder.timeLabel.setText(mTimeDataSet.get(position));
		viewHolder.secondLabel.setText("" + mPointDataSet.get(position));

		if (mAuthorDataSet != null) {
			viewHolder.userLabel.setText(mAuthorDataSet.get(position));
		}
		if (secondaryDataSet != null) {
			if (secondaryDataSet.get(0) != -1) {
				viewHolder.secondLabel.setText(""
						+ secondaryDataSet.get(position));
			} else {
				viewHolder.secondLabel.setText("");
			}
		}
	}

	@Override
	public int getItemCount() {
		return mDataSet.size();
	}

	public final static class ListItemViewHolder extends
			RecyclerView.ViewHolder {
		TextView label;
		TextView secondLabel;
		TextView userLabel;
		TextView timeLabel;

		public ListItemViewHolder(View itemView) {
			super(itemView);
			label = (TextView) itemView.findViewById(R.id.topText);
			secondLabel = (TextView) itemView.findViewById(R.id.secondaryText);
			userLabel = (TextView) itemView.findViewById(R.id.middleText);
			timeLabel = (TextView) itemView.findViewById(R.id.bottomText);
		}
	}
}