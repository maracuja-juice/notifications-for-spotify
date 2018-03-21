package com.maracuja_juice.spotifynotifications.com.maracuja_juice.spotifynotifications.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.maracuja_juice.spotifynotifications.R;
import com.maracuja_juice.spotifynotifications.helper.ReleaseDateParser;
import com.maracuja_juice.spotifynotifications.services.OnTaskCompleted;
import com.maracuja_juice.spotifynotifications.services.SpotifyCrawlerTask;

import org.joda.time.LocalDate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kaaes.spotify.webapi.android.models.Album;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted {

    private static final String LOG_TAG = MainActivity.class.getName();
    private boolean isLoggedIn = false;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isLoggedIn) {
            Intent intent = new Intent(this, LoginActivity.class);
            int requestCode = 1;
            startActivityForResult(intent, requestCode);
        } else {
            startSpotifyCrawlerTask();
        }
    }

    private void startSpotifyCrawlerTask() {
        new SpotifyCrawlerTask(token, this).execute();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                isLoggedIn = true;
                token = data.getStringExtra("token");
                startSpotifyCrawlerTask();
            }
        }
    }

    @Override
    public void onTaskCompleted(Object result) {
        // TODO: add progress bar.
        List<Album> albums = (List<Album>) result;
        Collections.sort(albums, new Comparator<Album>() {
            public int compare(Album album1, Album album2) {
                LocalDate releaseDate1 = ReleaseDateParser.parseReleaseDate(
                        album1.release_date,
                        album1.release_date_precision);
                LocalDate releaseDate2 = ReleaseDateParser.parseReleaseDate(
                        album2.release_date,
                        album2.release_date_precision);
                return releaseDate2.compareTo(releaseDate1);
            }
        });
        ListView listView = findViewById(R.id.albumListView);
        AlbumListAdapter adapter = new AlbumListAdapter(this, albums);
        listView.setAdapter(adapter);

    }
}
