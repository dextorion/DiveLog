package com.divelog.activity.logentry;

import java.util.List;

import com.divelog.R;
import com.divelog.db.model.Logentry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LogentryAdapter extends BaseAdapter {

	private Context context;
	LayoutInflater inflater;
	
	private List<Logentry> logEntryList;
	
	static class LogEntryViewHolder {
	    TextView id;
	    TextView date;
	    TextView diveSite;
	}
	
	public LogentryAdapter(Context context, List<Logentry> logEntryList) {
		this.context = context;
		this.inflater = LayoutInflater.from(this.context);
		
		this.logEntryList = logEntryList;
	}

    public List<Logentry> getLogentryList() {
        return logEntryList;
    }
	
	public int getCount() {
		return this.logEntryList.size();
	}

	public Object getItem(int position) {
		return this.logEntryList.get(position);
	}

	public long getItemId(int position) {
		return this.logEntryList.get(position).getNum();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LogEntryViewHolder viewHolder;
		
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.logentry_listitem_layout, null);
			viewHolder = new LogEntryViewHolder();
			viewHolder.id = (TextView)convertView.findViewById(R.id.logentry_listitem_id);
			viewHolder.date = (TextView)convertView.findViewById(R.id.logentry_listitem_date);
			viewHolder.diveSite = (TextView)convertView.findViewById(R.id.logentry_listitem_dive_site);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (LogEntryViewHolder) convertView.getTag();
		}
		
		viewHolder.id.setText(String.valueOf(this.logEntryList.get(position).getNum()));
		viewHolder.date.setText(this.logEntryList.get(position).getDate().format("%G-%m-%d"));
		viewHolder.diveSite.setText(this.logEntryList.get(position).getDiveSite().getName());
		
		return convertView;
	}

}
