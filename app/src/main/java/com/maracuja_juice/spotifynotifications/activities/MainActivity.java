package com.maracuja_juice.spotifynotifications.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.maracuja_juice.spotifynotifications.R;
import com.maracuja_juice.spotifynotifications.adapters.AlbumListAdapter;
import com.maracuja_juice.spotifynotifications.fragments.LoginFragment;
import com.maracuja_juice.spotifynotifications.fragments.ProgressBarFragment;
import com.maracuja_juice.spotifynotifications.interfaces.LoginListener;
import com.maracuja_juice.spotifynotifications.model.LoginResult;
import com.maracuja_juice.spotifynotifications.model.MyAlbum;
import com.maracuja_juice.spotifynotifications.services.OnTaskCompleted;
import com.maracuja_juice.spotifynotifications.services.SpotifyCrawlerTask;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted, LoginListener {

    private static final String LOG_TAG = MainActivity.class.getName();
    private boolean isLoggedIn = false;
    private String token;

    private FragmentManager fragmentManager;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ProgressBarFragment progressBarFragment;
    private LoginFragment loginFragment;

    // TODO: add filter button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        progressBarFragment = (ProgressBarFragment) fragmentManager.findFragmentById(R.id.fragmentProgressBar);

        if (!isLoggedIn) {
            login();
        } else {
            startSpotifyCrawlerTask();
        }
    }

    private void login() {
        loginFragment = new LoginFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(loginFragment, "login").commitNow();

        loginFragment.startLogin(this);
    }

    @Override
    public void loginFinished(LoginResult result) {
        token = result.getToken();
        startSpotifyCrawlerTask();

        fragmentManager.beginTransaction().remove(loginFragment).commitNow();
        loginFragment = null;
    }

    private void startSpotifyCrawlerTask() {
        progressBarFragment.showProgressBar();
        new SpotifyCrawlerTask(token, this).execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Login Finished. This is not really nice but I haven't found a better way.
        if (loginFragment != null) {
            loginFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onTaskCompleted(Object result) {
        progressBarFragment.hideProgressBar();

        List<MyAlbum> albums = (List<MyAlbum>) result;

        recyclerView = findViewById(R.id.albumListView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AlbumListAdapter(this, albums);
        recyclerView.setAdapter(adapter);
    }
}
