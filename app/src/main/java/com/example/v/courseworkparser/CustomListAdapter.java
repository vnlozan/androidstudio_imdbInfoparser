package com.example.v.courseworkparser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by V on 05.06.2016.
 */
public class CustomListAdapter extends ArrayAdapter<fullMovieInfo> {
    private final Context context;
    private final int resourceId;
    public CustomListAdapter(Context context, int resource) {
        super(context, resource);
        this.resourceId = resource;
        this.context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final fullMovieInfo fullMovieInfo = getItem(position);
        shortMovieInfo shortMovieInfo = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resourceId, null);
            shortMovieInfo = new shortMovieInfo((ImageView) convertView.findViewById(R.id.imageView),(TextView) convertView.findViewById(R.id.textView));
            convertView.setTag(shortMovieInfo);
        } else {
            shortMovieInfo = (shortMovieInfo) convertView.getTag();
        }
        shortMovieInfo.setTextView("Title : "+fullMovieInfo.getTitleMovie()
                +"\n Year : " +fullMovieInfo.getYearMovie()
                +"\n Rating : " +fullMovieInfo.getImdbRatingMovie());
        shortMovieInfo.setImageView(fullMovieInfo.getImage());
        return convertView;
    }
}