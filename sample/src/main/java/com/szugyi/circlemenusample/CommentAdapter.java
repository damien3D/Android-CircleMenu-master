package com.szugyi.circlemenusample;

import android.content.Context;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
//import com.glifit.app.AppController;

import java.util.ArrayList;

public class CommentAdapter extends ArrayAdapter<Comments> {

	ArrayList<Comments> commentList;

	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	
	public CommentAdapter(Context context, int resource, ArrayList<Comments> commentList){
		super(context, resource, commentList);
		this.commentList = new ArrayList<>();
		this.commentList.addAll(commentList);
	}
	
	@Override
	public View getView( int position, View convertView, ViewGroup parent){

		ViewHolder holder;
		
		Log.v("ConvertView", String.valueOf(position));
		
		if (convertView == null) {
			
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = vi.inflate(R.layout.row_comments, null);

			holder = new ViewHolder();
			
			holder.list = (RelativeLayout) convertView.findViewById(R.id.relativeLayout1); 
			holder.author = (TextView) convertView.findViewById(R.id.author);
			holder.commentmessage = (TextView) convertView.findViewById(R.id.comment);
			
			convertView.setTag(holder);
			
			holder.list.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					
					Log.v("list","click");
					
				}
			});
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Comments comment = commentList.get(position);
		
		holder.author.setText(comment.getAuthor());
		holder.commentmessage.setText(comment.getMessage());
		Linkify.addLinks(holder.commentmessage, Linkify.ALL);

		return convertView;
	}
	
	static class ViewHolder {
		public TextView author;
		public TextView id;
		public RelativeLayout list;
		public TextView commentmessage;
		
	}

}
