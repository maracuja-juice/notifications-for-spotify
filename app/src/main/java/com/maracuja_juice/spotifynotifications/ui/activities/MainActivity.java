package com.maracuja_juice.spotifynotifications.ui.activities;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.maracuja_juice.spotifynotifications.R;
import com.maracuja_juice.spotifynotifications.helper.AppInstallationHelper;
import com.maracuja_juice.spotifynotifications.ui.adapters.AlbumListAdapter;
import com.maracuja_juice.spotifynotifications.ui.fragments.LoginFragment;
import com.maracuja_juice.spotifynotifications.ui.fragments.ProgressBarFragment;
import com.maracuja_juice.spotifynotifications.interfaces.DownloadCompleted;
import com.maracuja_juice.spotifynotifications.interfaces.LoginListener;
import com.maracuja_juice.spotifynotifications.model.LoginResult;
import com.maracuja_juice.spotifynotifications.model.MyAlbum;
import com.maracuja_juice.spotifynotifications.model.StartupPreferences;
import com.maracuja_juice.spotifynotifications.services.SpotifyCrawlerTask;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Album;

import static com.maracuja_juice.spotifynotifications.database.dao.MyAlbumDao.mergeAndSaveAlbums;
import static com.maracuja_juice.spotifynotifications.database.dao.MyAlbumDao.subscribeToMyAlbumList;
import static com.maracuja_juice.spotifynotifications.database.dao.StartupPreferencesDao.getStartupPreferences;
import static com.maracuja_juice.spotifynotifications.database.dao.StartupPreferencesDao.updateStartupPreferences;

public class MainActivity extends AppCompatActivity implements DownloadCompleted, LoginListener, AlbumListAdapter.OnItemClicked {

    private static final String LOG_TAG = MainActivity.class.getName();

    private String token;

    private FragmentManager fragmentManager;
    private AlbumListAdapter recyclerViewAdapter;

    private ProgressBarFragment progressBarFragment;
    private LoginFragment loginFragment;

    private StartupPreferences startupPreferences;

    private MenuItem refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startupInitialization();
        subscribeToMyAlbumList(this::updateList);

        boolean needToDownload = startupPreferences.needToDownload();
        boolean needToLogin = startupPreferences.needToLogin();
        String savedToken = startupPreferences.getToken();
        if (savedToken != null) {
            token = savedToken;
        }

        determineWhatToDoOnStartup(needToDownload, needToLogin);
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

    private void determineWhatToDoOnStartup(boolean needToDownload, boolean needToLogin) {
        if (needToDownload) {
            if (needToLogin) {
                Log.d(LOG_TAG, "logging in...");
                loginAndDownload();
            } else {
                download();
            }
        }
    }

    private void loginAndDownload() {
        loginFragment = new LoginFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(loginFragment, "loginAndDownload").commitNow();

        loginFragment.startLogin(this);
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
    public void loginFinished(LoginResult result) {
        token = result.getToken();
        int expiresIn = result.getTokenExpirationIn();
        saveValuesFromLoginToPreferences(token, expiresIn);

        download();

        fragmentManager.beginTransaction().remove(loginFragment).commitNow();
        loginFragment = null;
    }

    private void saveValuesFromLoginToPreferences(String token, int tokenExpiresInSeconds) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tokenExpiration = now.plusSeconds(tokenExpiresInSeconds);

        startupPreferences.setTokenExpiration(tokenExpiration);
        startupPreferences.setToken(token);
        updateStartupPreferences(startupPreferences);
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
        recyclerViewAdapter.setOnClick(MainActivity.this); // Bind the listener
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
    public void onItemClick(int position) {
        MyAlbum myAlbum = recyclerViewAdapter.getMyAlbum(position);

        boolean isSpotifyInstalled = AppInstallationHelper.isSpotifyInstalled(getPackageManager());
        if (isSpotifyInstalled) {
            Intent intent = new Intent(Intent.ACTION_VIEW);

            intent.setData(Uri.parse(myAlbum.getAlbum().uri));
            intent.putExtra(Intent.EXTRA_REFERRER,
                    Uri.parse("android-app://" + getApplicationContext().getPackageName()));
            startActivity(intent);
        } else {
            Toast.makeText(this, "Error: Please install the Spotify app.", Toast.LENGTH_SHORT).show();
        }
    }
}
