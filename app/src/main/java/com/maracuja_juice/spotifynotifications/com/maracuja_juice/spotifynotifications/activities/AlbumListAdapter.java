package com.maracuja_juice.spotifynotifications.com.maracuja_juice.spotifynotifications.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maracuja_juice.spotifynotifications.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Album;

/**
 * Created by Maurice on 03.03.18.
 */

public class AlbumListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Album> mDataSource;

    public AlbumListAdapter(Context context, List<Album> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.album_list, parent, false);

        // Get title element
        TextView titleTextView =
                (TextView) rowView.findViewById(R.id.album_list_title);

// Get subtitle element
        TextView subtitleTextView =
                (TextView) rowView.findViewById(R.id.album_list_subtitle);


// Get thumbnail element
        ImageView thumbnailImageView =
                (ImageView) rowView.findViewById(R.id.album_list_thumbnail);

        Album album = (Album) getItem(position);

// 2
        titleTextView.setText(album.name);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < album.artists.size(); i++) {
            stringBuilder.append(album.artists.get(i).name);
            if(album.artists.size() > i+1)
              stringBuilder.append(", ");
        }
        subtitleTextView.setText(stringBuilder);

// 3
        Picasso.with(mContext).load(album.images.get(0).url).placeholder(R.mipmap.ic_launcher).into(thumbnailImageView);

        return rowView;
    }
}
