package com.maracuja_juice.spotifynotifications.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.maracuja_juice.spotifynotifications.R;
import com.maracuja_juice.spotifynotifications.api.service.AuthorizationListener;
import com.maracuja_juice.spotifynotifications.api.service.RefreshTokenListener;
import com.maracuja_juice.spotifynotifications.model.MyAlbum;
import com.maracuja_juice.spotifynotifications.model.StartupPreferences;
import com.maracuja_juice.spotifynotifications.services.DownloadCompleted;
import com.maracuja_juice.spotifynotifications.services.SpotifyCrawlerTask;
import com.maracuja_juice.spotifynotifications.ui.adapters.AlbumListAdapter;
import com.maracuja_juice.spotifynotifications.ui.fragments.AuthorizationFragment;
import com.maracuja_juice.spotifynotifications.ui.fragments.ProgressBarFragment;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Album;

import static com.maracuja_juice.spotifynotifications.database.dao.MyAlbumDao.mergeAndSaveAlbums;
import static com.maracuja_juice.spotifynotifications.database.dao.MyAlbumDao.subscribeToMyAlbumList;
import static com.maracuja_juice.spotifynotifications.database.dao.StartupPreferencesDao.getStartupPreferences;
import static com.maracuja_juice.spotifynotifications.database.dao.StartupPreferencesDao.updateStartupPreferences;

public class MainActivity extends AppCompatActivity implements DownloadCompleted, AuthorizationListener, RefreshTokenListener {

    private static final String LOG_TAG = MainActivity.class.getName();

    private String token;

    private FragmentManager fragmentManager;
    private RecyclerView.Adapter recyclerViewAdapter;

    private ProgressBarFragment progressBarFragment;
    private AuthorizationFragment loginFragment;

    private StartupPreferences startupPreferences;

    private MenuItem refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startupInitialization();
        subscribeToMyAlbumList(this::updateList);

        // TODO refactor this thing here?
        boolean needToAuthorize = startupPreferences.needToAuthorize();
        boolean needToDownload = startupPreferences.needToDownload();
        boolean needToLogin = startupPreferences.needToLogin();
        String savedToken = startupPreferences.getCurrentAccessToken();
        if (savedToken != null) {
            token = savedToken;
        }

        determineWhatToDoOnStartup(needToDownload, needToLogin, needToAuthorize);
    }

    private void startupInitialization() {
        fragmentManager = getSupportFragmentManager();
        progressBarFragment = (ProgressBarFragment) fragmentManager.findFragmentById(R.id.fragmentProgressBar);
        progressBarFragment.showProgressBar();
        setAdapter(new ArrayList<>());

        Toolbar toolbar = findViewById(R.id.mainActivityToolbar);
        setSupportActionBar(toolbar);

        startupPreferences = getStartupPreferences();
    }

    private void updateList(List<MyAlbum> data) {
        if (progressBarFragment != null) {
            progressBarFragment.hideProgressBar();
        }

        if (recyclerViewAdapter == null) {
            setAdapter(data);
        } else {
            AlbumListAdapter albumListAdapter = (AlbumListAdapter) recyclerViewAdapter;
            albumListAdapter.setDataSource(data);
            albumListAdapter.notifyDataSetChanged();
        }
    }

    private void determineWhatToDoOnStartup(boolean needToDownload, boolean needToLogin, boolean needToAuthorize) {
        if (needToDownload) {
            if (needToAuthorize) {
                authorizeAndDownload();
            } else if (needToLogin) {
                Log.d(LOG_TAG, "logging in...");
                loginAndDownload();
            } else {
                download();
            }
        }
    }

    private void authorizeAndDownload() {
        // TODO refactor with new names
        loginFragment = new AuthorizationFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(loginFragment, "loginAndDownload").commitNow();
        // TODO: ...
        loginFragment.startLogin();
    }

    private void loginAndDownload() {
        // TODO: call refresh token
    }

    private void download() {
        Log.d(LOG_TAG, "downloading");
        new SpotifyCrawlerTask(token, this).execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Login Finished. This is not really nice but I haven't found a better way.
        // Because otherwise the loginFragment.onActivityResult doesn't get called
        if (loginFragment != null) {
            loginFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void downloadComplete(List<Album> result) {
        Log.d(LOG_TAG, "finished downloading");
        if (progressBarFragment != null) {
            progressBarFragment.hideProgressBar();
        }
        if (refreshButton != null) {
            refreshButton.setEnabled(true);
            int normalColor = 255;
            refreshButton.getIcon().setAlpha(normalColor);
            refreshButton = null;
        }

        if (result != null) {
            mergeAndSaveAlbums(result);

            startupPreferences.setLastDownload(LocalDate.now()); // TODO mock or something?
            updateStartupPreferences(startupPreferences);
        }
    }

    private void setAdapter(List<MyAlbum> myAlbums) {
        RecyclerView recyclerView = findViewById(R.id.albumListView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerViewAdapter = new AlbumListAdapter(this, myAlbums);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void hardRefreshItems() {
        progressBarFragment.showProgressBar();

        startupPreferences = getStartupPreferences();
        if (startupPreferences.needToLogin()) {
            loginAndDownload();
        } else {
            download();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mainactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                hardRefreshItems();
                item.setEnabled(false);
                int greyIcon = 130;
                item.getIcon().setAlpha(greyIcon);
                refreshButton = item;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void authorizationFinished(String accessToken, String refreshToken, int secondsToExpiration) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tokenExpiration = now.plusSeconds(secondsToExpiration);
        startupPreferences.setTokenExpiration(tokenExpiration);
        startupPreferences.setCurrentAccessToken(token);
        // TODO: refresh token.
        updateStartupPreferences(startupPreferences);

        download();

        fragmentManager.beginTransaction().remove(loginFragment).commitNow();
        loginFragment = null;
    }

    @Override
    public void refreshFinished(String accessToken, int secondsToExpiration) {
        // TODO implement refresh finished
    }
}
