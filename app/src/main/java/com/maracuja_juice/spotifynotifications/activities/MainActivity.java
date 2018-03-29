package com.maracuja_juice.spotifynotifications.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.maracuja_juice.spotifynotifications.R;
import com.maracuja_juice.spotifynotifications.SpotifyNotificationsApplication;
import com.maracuja_juice.spotifynotifications.adapters.AlbumListAdapter;
import com.maracuja_juice.spotifynotifications.data.MyAlbum;
import com.maracuja_juice.spotifynotifications.data.StartupPreferences;
import com.maracuja_juice.spotifynotifications.fragments.LoginFragment;
import com.maracuja_juice.spotifynotifications.fragments.ProgressBarFragment;
import com.maracuja_juice.spotifynotifications.interfaces.LoginListener;
import com.maracuja_juice.spotifynotifications.model.LoginResult;
import com.maracuja_juice.spotifynotifications.services.OnTaskCompleted;
import com.maracuja_juice.spotifynotifications.services.SpotifyCrawlerTask;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted, LoginListener {

    private static final String LOG_TAG = MainActivity.class.getName();

    private String token;

    private Box<MyAlbum> myAlbumBox;
    private Box<StartupPreferences> startupPreferencesBox;

    private FragmentManager fragmentManager;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ProgressBarFragment progressBarFragment;
    private LoginFragment loginFragment;

    private StartupPreferences startupPreferences;
    // TODO: add filter button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BoxStore boxStore = ((SpotifyNotificationsApplication) getApplication()).getBoxStore();
        myAlbumBox = boxStore.boxFor(MyAlbum.class);
        startupPreferencesBox = boxStore.boxFor(StartupPreferences.class);
        fragmentManager = getSupportFragmentManager();
        progressBarFragment = (ProgressBarFragment) fragmentManager.findFragmentById(R.id.fragmentProgressBar);
        startupPreferences = getStartupPreferences();

        boolean needToLogin = true;
        boolean needToDownload = true;
        if(startupPreferences != null) {
            // TODO: mock this datetime or something? Or somehow else make it adjustable for tests
            needToLogin = startupPreferences.needToLogin(LocalDateTime.now());
            needToDownload = startupPreferences.needToDownload(LocalDate.now().minusDays(1));
            token = startupPreferences.getToken();
        } else {
            startupPreferences = new StartupPreferences();
        }

        if (needToDownload) {
            if (needToLogin) {
                login();
            } else {
                startSpotifyCrawlerTask();
            }
        } else {
            // TODO: remove performance report.
            long time1 = System.nanoTime();
            List<MyAlbum> myAlbums = myAlbumBox.getAll(); // TODO: background task???
            long time2 = System.nanoTime();
            Log.i(LOG_TAG, String.valueOf(time2 - time1) + " for " + myAlbums.size() + " items.");

            setAdapter(myAlbums);
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
        String token = result.getToken();
        int expiresIn = result.getTokenExpirationIn();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tokenExpiration = now.plusSeconds(expiresIn);
        saveValuesFromLoginToPreferences(token, tokenExpiration);

        startSpotifyCrawlerTask();

        fragmentManager.beginTransaction().remove(loginFragment).commitNow();
        loginFragment = null;
    }

    private void saveValuesFromLoginToPreferences(String token, LocalDateTime tokenExpiration) {
        this.token = token;

        startupPreferences.setTokenExpiration(tokenExpiration);
        startupPreferences.setToken(token);
        startupPreferencesBox.put(startupPreferences);
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

        setAdapter((List<MyAlbum>) result);

        // TODO don't save twice -> merge!
        // TODO:  performance! -> separate thread so that ui doesn't block?
        myAlbumBox.removeAll(); // TODO remove this line later.
        myAlbumBox.put((List<MyAlbum>) result);

        startupPreferences.setLastDownload(LocalDate.now());
        startupPreferencesBox.put(startupPreferences);
    }

    private void setAdapter(List<MyAlbum> myAlbums) {
        recyclerView = findViewById(R.id.albumListView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AlbumListAdapter(this, myAlbums);
        recyclerView.setAdapter(adapter);
    }

    private StartupPreferences getStartupPreferences() {
        List<StartupPreferences> allPreferences = startupPreferencesBox.getAll();
        StartupPreferences preferences = null;
        if(allPreferences.size() != 0) {
            preferences = allPreferences.get(0);
        }
        return preferences;
    }
}
