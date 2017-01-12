package com.main.Scrawl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class SpinnerAdapter extends ArrayAdapter<String> {

	public SpinnerAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
	}

	private Context ctx;
	private String[] contentArray;

	//public SpinnerAdapter(Context context, int resource, String[] objects) {
		//super(context,  R.layout.spinner_value_layout, R.id.spinnerTextView, objects);

		//this.ctx = context;
		//this.contentArray = objects;
	//}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.spinner_value_layout, parent,
				false);

		//TextView textView = (TextView) row.findViewById(R.id.spinnerTextView);
		//textView.setText(contentArray[position]);

		return row;
	}

}
