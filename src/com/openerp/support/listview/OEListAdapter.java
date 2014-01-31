package com.openerp.support.listview;

import java.util.ArrayList;
import java.util.List;

import com.openerp.util.logger.OELog;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Filter;

public class OEListAdapter extends ArrayAdapter<Object> {
	Context mContext = null;
	List<Object> mObjects = null;
	List<Object> mAllObjects = null;
	RowFilter mFilter = null;
	int mResourceId = 0;

	public OEListAdapter(Context context, int resource, List<Object> objects) {
		super(context, resource, objects);
		mContext = context;
		mObjects = new ArrayList<Object>(objects);
		mAllObjects = new ArrayList<Object>(objects);
		mResourceId = resource;
	}

	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new RowFilter();
		}
		return mFilter;
	}

	public int getResource() {
		return mResourceId;
	}

	public void notifiyDataChange(List<Object> objects) {
		mAllObjects = new ArrayList<Object>(objects);
		mObjects = new ArrayList<Object>(objects);
		notifyDataSetChanged();
	}

	class RowFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults result = new FilterResults();
			if (!TextUtils.isEmpty(constraint)) {
				String searchingStr = constraint.toString().toLowerCase();
				List<Object> filteredItems = new ArrayList<Object>();
				for (Object item : mAllObjects) {
					if (item.toString().toLowerCase().contains(searchingStr)) {
						filteredItems.add(item);
					}
				}
				result.count = filteredItems.size();
				result.values = filteredItems;
			} else {
				synchronized (this) {
					result.count = mAllObjects.size();
					result.values = mAllObjects;
				}
			}
			return result;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			if (results.count > 0) {
				clear();
				mObjects = (List<Object>) results.values;
				addAll(mObjects);
			}
			notifyDataSetChanged();
		}
	}

}
