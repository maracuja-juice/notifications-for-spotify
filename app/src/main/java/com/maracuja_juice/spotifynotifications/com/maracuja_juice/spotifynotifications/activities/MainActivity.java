package com.maracuja_juice.spotifynotifications.com.maracuja_juice.spotifynotifications.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.maracuja_juice.spotifynotifications.R;
import com.maracuja_juice.spotifynotifications.services.OnTaskCompleted;
import com.maracuja_juice.spotifynotifications.services.SpotifyCrawlerTask;

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

        if(!isLoggedIn) {
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
            if(resultCode == RESULT_OK) {
                isLoggedIn = true;
                token = data.getStringExtra("token");
                startSpotifyCrawlerTask();
            }
        }
    }

    @Override
    public void onTaskCompleted(Object result) {
        List<Album> albums = (List<Album>) result;
        for (int i = 0; i < albums.size(); i++) {
            Log.d(LOG_TAG, albums.get(i).name);
        }
        ListView listView = findViewById(R.id.albumListView);
        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, albums);
        listView.setAdapter(adapter);

    }
}
