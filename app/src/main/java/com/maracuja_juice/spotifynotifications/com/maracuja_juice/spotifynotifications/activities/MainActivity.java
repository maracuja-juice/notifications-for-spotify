package com.maracuja_juice.spotifynotifications.com.maracuja_juice.spotifynotifications.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.maracuja_juice.spotifynotifications.R;
import com.maracuja_juice.spotifynotifications.services.SpotifyCrawler;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

public class MainActivity extends AppCompatActivity {

    private boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle receiveBundle = this.getIntent().getExtras();
        if(receiveBundle != null) {
            String token = receiveBundle.getString("token");
            int expiresIn = receiveBundle.getInt("expiresIn");
            isLoggedIn = true;

            SpotifyCrawler crawler = new SpotifyCrawler(token, expiresIn);
            List<Artist> artists = crawler.getArtists();
        }

        if(!isLoggedIn) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
