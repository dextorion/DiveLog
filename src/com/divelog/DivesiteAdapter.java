package com.divelog;

import java.util.List;

import com.divelog.model.Divesite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DivesiteAdapter extends BaseAdapter {

	private Context context;
	LayoutInflater inflater;
	
	private List<Divesite> divesiteList;
	
	static class DivesiteViewHolder {
	    TextView id;
	    TextView name;
	}
	
	public DivesiteAdapter(Context context, List<Divesite> divesiteList) {
		this.context = context;
		this.inflater = LayoutInflater.from(this.context);
		
		this.divesiteList = divesiteList;
	}
	
	public int getCount() {
		return this.divesiteList.size();
	}

	public Object getItem(int position) {
		return this.divesiteList.get(position);
	}

	public long getItemId(int position) {
		return this.divesiteList.get(position).getId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		DivesiteViewHolder viewHolder;
		
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.divesite_listitem_layout, null);
			viewHolder = new DivesiteViewHolder();
			viewHolder.id = (TextView)convertView.findViewById(R.id.divesite_listitem_id);
			viewHolder.name = (TextView)convertView.findViewById(R.id.divesite_listitem_name);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (DivesiteViewHolder) convertView.getTag();
		}
		
		viewHolder.id.setText(String.valueOf(this.divesiteList.get(position).getId()));
		viewHolder.name.setText(this.divesiteList.get(position).getName());
		
		return convertView;
	}

}
