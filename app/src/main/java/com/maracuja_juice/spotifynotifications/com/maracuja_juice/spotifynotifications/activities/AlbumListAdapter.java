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

import java.util.List;

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.ArtistSimple;

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
        // TODO: sort by release date -> use release date parser
        // TODO: don't show full list. only first 100 items probably (out of memory error)
        // TODO: add filter button

        View rowView = mInflater.inflate(R.layout.album_list, parent, false);
        ImageView thumbnailImageView = rowView.findViewById(R.id.album_list_thumbnail);
        TextView titleTextView = rowView.findViewById(R.id.album_list_title);
        TextView artistTextView = rowView.findViewById(R.id.album_list_artists);
        TextView releaseDateTextView = rowView.findViewById(R.id.album_list_release_date);

        Album album = (Album) getItem(position);
        titleTextView.setText(album.name);

        String artistText = getArtistText(album.artists);
        artistTextView.setText(artistText);

        releaseDateTextView.setText(album.release_date);

        String imageUrl = album.images.get(0).url;
        // TODO: Add better placeholder image
        Picasso.with(mContext).load(imageUrl).placeholder(R.mipmap.ic_launcher).into(thumbnailImageView);

        return rowView;
    }

    private String getArtistText(List<ArtistSimple> artists) {
        StringBuilder artistText = new StringBuilder();
        for (int i = 0; i < artists.size(); i++) {
            artistText.append(artists.get(i).name);
            boolean thereAreMoreArtists = artists.size() > i+1;
            if(thereAreMoreArtists)
                artistText.append(", ");
        }
        return artistText.toString();
    }
}