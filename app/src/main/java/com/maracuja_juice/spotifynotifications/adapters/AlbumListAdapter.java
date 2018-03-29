package com.maracuja_juice.spotifynotifications.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maracuja_juice.spotifynotifications.R;
import com.maracuja_juice.spotifynotifications.data.MyAlbum;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import kaaes.spotify.webapi.android.models.Album;

/**
 * Created by Maurice on 03.03.18.
 */

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.ViewHolder> {
    private List<MyAlbum> dataSource;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // TODO: onclick would be handled here.

        public ImageView albumArt;
        public TextView albumTitle;
        public TextView artists;
        public TextView releaseDate;

        public ViewHolder(View v) {
            super(v);
            albumArt = v.findViewById(R.id.album_list_album_art);
            albumTitle = v.findViewById(R.id.album_list_title);
            artists = v.findViewById(R.id.album_list_artists);
            releaseDate = v.findViewById(R.id.album_list_release_date);
        }
    }

    public AlbumListAdapter(Context context, List<MyAlbum> items) {
        setDataSource(items);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_list, parent, false);
        ViewHolder vh = new ViewHolder(rowView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyAlbum myAlbum = dataSource.get(position);
        Album album = myAlbum.getAlbum();

        holder.albumTitle.setText(album.name);
        holder.releaseDate.setText(myAlbum.getReleaseDate().toString());
        holder.artists.setText(myAlbum.getArtistText());
        Picasso.with(context).load(myAlbum.getImageUrl())
                .placeholder(R.drawable.placeholder_album).into(holder.albumArt);

    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public void setDataSource(List<MyAlbum> albums) {
        Collections.sort(albums);
        dataSource = albums;
    }
}
