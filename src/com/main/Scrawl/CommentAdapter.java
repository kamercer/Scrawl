package com.main.Scrawl;

import java.util.ArrayList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CommentAdapter extends
		RecyclerView.Adapter<CommentAdapter.ListItemViewHolder> {

	private ArrayList<String> mDataSet = new ArrayList<String>();
	private ArrayList<String> mAuthorDataSet = new ArrayList<String>();

	public CommentAdapter(ArrayList<String> dataSet, ArrayList<String> authorDataSet) {
		
		mDataSet = dataSet;
		mAuthorDataSet = authorDataSet;
	}

	@Override
	public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup,
			int viewType) {
		View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
				R.layout.comment_adapter_text_view, viewGroup, false);

		return new ListItemViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(ListItemViewHolder viewHolder, int position) {
		viewHolder.label.setText(mDataSet.get(mDataSet.size()-position-1));
		viewHolder.userLabel.setText(mAuthorDataSet.get(mAuthorDataSet.size()-position-1));
	}

	@Override
	public int getItemCount() {
		return mDataSet.size();
	}

	public final static class ListItemViewHolder extends
			RecyclerView.ViewHolder {
		TextView label;
		TextView userLabel;

		public ListItemViewHolder(View itemView) {
			super(itemView);
			label = (TextView) itemView.findViewById(R.id.comment);
			userLabel = (TextView) itemView.findViewById(R.id.commentUser);
		}
	}
}