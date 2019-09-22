package com.vulcan.invite;


import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ActorsAdapter extends ArrayAdapter<Actors> {

	ArrayList<Actors> ArrayListActors;
	int Resource;
	Context context;
	LayoutInflater vi;
	
	public ActorsAdapter(Context context, int resource,
			ArrayList<Actors> objects) {
		
		super(context, resource, objects);

		ArrayListActors = objects;
		Resource = resource;
		this.context = context;
		
		vi=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if(convertView == null)
		{
			convertView = vi.inflate(Resource,  null);
			holder = new ViewHolder();
	
			holder.month = (TextView) convertView.findViewById(R.id.textView1);
			holder.year = (TextView) convertView.findViewById(R.id.textView2);
			holder.date = (TextView) convertView.findViewById(R.id.textView3);
			holder.event = (TextView) convertView.findViewById(R.id.textView4);
			holder.time = (TextView) convertView.findViewById(R.id.textView5);
			holder.organiser = (TextView) convertView.findViewById(R.id.textView6);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.month.setText(ArrayListActors.get(position).getMonth());
		holder.year.setText(ArrayListActors.get(position).getYear());
		holder.date.setText(ArrayListActors.get(position).getDate());
		holder.event.setText(ArrayListActors.get(position).getEvent());
		holder.time.setText(ArrayListActors.get(position).getTime());
		holder.organiser.setText( ArrayListActors.get(position).getOrganiser());
		return convertView;
	}

	static class ViewHolder {
		public TextView month;
		public TextView year;
		public TextView date;
		public TextView event;
		public TextView time;
		public TextView organiser;

	}
}
