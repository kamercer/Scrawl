package com.main.Scrawl;

import java.util.ArrayList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CreateSelectorAdapter extends
		RecyclerView.Adapter<CreateSelectorAdapter.ListItemViewHolder> {

	private ArrayList<String> mDataSet;

	CreateSelectorAdapter(ArrayList<String> dataSet) {
		if (dataSet == null) {
			throw new IllegalArgumentException("modelData must not be null");
		}
		mDataSet = dataSet;
	}

	@Override
	public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup,
			int viewType) {
		View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
				R.layout.creater_selector_text_view, viewGroup, false);

		return new ListItemViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(ListItemViewHolder viewHolder, int position) {
		viewHolder.label.setText(mDataSet.get(position));
	}

	@Override
	public int getItemCount() {
		return mDataSet.size();
	}

	public final static class ListItemViewHolder extends
			RecyclerView.ViewHolder {
		TextView label;

		public ListItemViewHolder(View itemView) {
			super(itemView);
			label = (TextView) itemView.findViewById(R.id.text);
		}
	}
}