package com.maracuja_juice.spotifynotifications.com.maracuja_juice.spotifynotifications.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.maracuja_juice.spotifynotifications.R;
import com.maracuja_juice.spotifynotifications.model.MyAlbum;
import com.maracuja_juice.spotifynotifications.services.OnTaskCompleted;
import com.maracuja_juice.spotifynotifications.services.SpotifyCrawlerTask;

import java.util.Collections;
import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted {

    private static final String LOG_TAG = MainActivity.class.getName();
    private boolean isLoggedIn = false;
    private String token;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // TODO: add filter button

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
        showProgressBar();
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
        hideProgressBar();

        List<MyAlbum> albums = (List<MyAlbum>) result;
        Collections.sort(albums);

        mRecyclerView = findViewById(R.id.albumListView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new AlbumListAdapter(this, albums);
        mRecyclerView.setAdapter(mAdapter);

    }

    void hideProgressBar() {
        CircularProgressBar progressBar = (CircularProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }

    void showProgressBar() {
        CircularProgressBar progressBar = (CircularProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }
}
