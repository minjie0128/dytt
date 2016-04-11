package com.google.alading.client.android;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.alading.ee.R;

public class DropdownListAdapter extends BaseAdapter {

	private ArrayList<String> mListItems;
	private LayoutInflater mInflater;
	private ViewHolder mHolder;
	private static String selected = "";

	public static String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		DropdownListAdapter.selected = selected;
	}

	public DropdownListAdapter(Context context, ArrayList<String> items,
			TextView tv) {
		mListItems = new ArrayList<String>();
		mListItems.addAll(items);
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mListItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.drop_down_list_row, null);
			mHolder = new ViewHolder();
			mHolder.tv = (TextView) convertView
					.findViewById(R.DropDownList.SelectOption);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		mHolder.tv.setText(mListItems.get(position));

		return convertView;
	}

	private class ViewHolder {
		TextView tv;
	}
}
