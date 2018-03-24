package com.maracuja_juice.spotifynotifications.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.joda.time.LocalDateTime;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted, LoginListener {

    private static final String LOG_TAG = MainActivity.class.getName();

    private String token;

    private FragmentManager fragmentManager;
    private SharedPreferences sharedPreferences;

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

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        fragmentManager = getSupportFragmentManager();
        progressBarFragment = (ProgressBarFragment) fragmentManager.findFragmentById(R.id.fragmentProgressBar);

        boolean isLoggedIn = sharedPreferences.getBoolean(getString(R.string.isLoggedIn), false);
        String expiration = sharedPreferences.getString(getString(R.string.expirationTime), null);
        LocalDateTime expirationTime = LocalDateTime.parse(expiration);
        token = sharedPreferences.getString(getString(R.string.token), token);

        boolean tokenIsExpired = LocalDateTime.now().isAfter(expirationTime);
        if (!isLoggedIn || tokenIsExpired) {
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
        int expiresIn = result.getTokenExpirationIn();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tokenExpiration = now.plusSeconds(expiresIn);
        saveValuesFromLoginToPreferences(tokenExpiration);

        startSpotifyCrawlerTask();

        fragmentManager.beginTransaction().remove(loginFragment).commitNow();
        loginFragment = null;
    }

    private void saveValuesFromLoginToPreferences(LocalDateTime tokenExpiration) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.expirationTime), tokenExpiration.toString());
        editor.putString(getString(R.string.token), token);
        editor.putBoolean(getString(R.string.isLoggedIn), true);
        editor.commit();
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

        // TODO: would need to save to database here so that the state gets persisted well.
        // -> no need to re get all the values.
    }
}
