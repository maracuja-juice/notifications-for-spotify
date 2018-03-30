package com.maracuja_juice.spotifynotifications.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
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

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidScheduler;
import io.objectbox.query.Query;
import kaaes.spotify.webapi.android.models.Album;

import static com.maracuja_juice.spotifynotifications.helper.AlbumToMyAlbumConverter.convertAlbumsToMyAlbums;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted, LoginListener {

    private static final String LOG_TAG = MainActivity.class.getName();

    private String token;

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

        fragmentManager = getSupportFragmentManager();
        progressBarFragment = (ProgressBarFragment) fragmentManager.findFragmentById(R.id.fragmentProgressBar);
        progressBarFragment.showProgressBar();
        setAdapter(new ArrayList<>());

        startupPreferencesBox = getBoxStore().boxFor(StartupPreferences.class);
        startupPreferences = getStartupPreferences();

        Box<MyAlbum> myAlbumBox = getBoxStore().boxFor(MyAlbum.class);
        Query<MyAlbum> query = myAlbumBox.query().build();
        query.subscribe().on(AndroidScheduler.mainThread()).observer(data -> updateList(data));

        // TODO separate method
        boolean needToLogin = true;
        boolean needToDownload = true;
        if (startupPreferences != null) {
            // TODO: mock this datetime or something? Or somehow else make it adjustable for tests
            needToLogin = startupPreferences.needToLogin(LocalDateTime.now());
            needToDownload = startupPreferences.needToDownload(LocalDate.now());
            token = startupPreferences.getToken();
        } else {
            startupPreferences = new StartupPreferences();
        }

        // TODO separate method
        if (needToDownload) {
            if (needToLogin) {
                Log.d(LOG_TAG, "logging in...");
                login();
            } else {
                startSpotifyCrawlerTask();
            }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Login Finished. This is not really nice but I haven't found a better way.
        if (loginFragment != null) {
            loginFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void saveValuesFromLoginToPreferences(String token, LocalDateTime tokenExpiration) {
        this.token = token;

        startupPreferences.setTokenExpiration(tokenExpiration);
        startupPreferences.setToken(token);
        startupPreferencesBox.put(startupPreferences);
    }

    private void startSpotifyCrawlerTask() {
        Log.d(LOG_TAG, "downloading");
        new SpotifyCrawlerTask(token, this).execute();
    }

    @Override
    public void onTaskCompleted(Object result) {
        Log.d(LOG_TAG, "finished downloading");

        saveAlbumsToDatabase((List<Album>) result);

        startupPreferences.setLastDownload(LocalDate.now()); // TODO
        startupPreferencesBox.put(startupPreferences);
    }

    // TODO database actions should be in separate class (or something else)
    private void saveAlbumsToDatabase(List<Album> allAlbums) {
        getBoxStore().runInTxAsync(() -> {
                    BoxStore store = getBoxStore();
                    Box<MyAlbum> albumBox = store.boxFor(MyAlbum.class);

                    List<MyAlbum> savedMyAlbums = albumBox.getAll();
                    List<Album> albums = new ArrayList<>();
                    Stream.of(savedMyAlbums).forEach(e -> albums.add(e.getAlbum()));

                    List<Album> newAlbums = Stream.of(allAlbums).filter(downloadedAlbum -> {
                        boolean albumIsAlreadySaved = Stream.of(albums)
                                .anyMatch(album -> album.id.equals(downloadedAlbum.id));
                        return !albumIsAlreadySaved;
                    }).collect(Collectors.toList());

                    Log.d(LOG_TAG, "there are " + newAlbums.size() + " new albums");
                    if(newAlbums.size() > 0) {
                        List<MyAlbum> newMyAlbums = convertAlbumsToMyAlbums(newAlbums);
                        albumBox.put(newMyAlbums);
                    }
                }, null
        );
    }

    private void setAdapter(List<MyAlbum> myAlbums) {
        recyclerView = findViewById(R.id.albumListView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AlbumListAdapter(this, myAlbums);
        recyclerView.setAdapter(adapter);
    }

    private void updateList(List<MyAlbum> data) {
        if(progressBarFragment != null) {
            progressBarFragment.hideProgressBar();
        }

        if(adapter == null) {
            setAdapter(data);
        } else {
            AlbumListAdapter adapter = (AlbumListAdapter) recyclerView.getAdapter();
            adapter.setDataSource(data);
            adapter.notifyDataSetChanged();
        }
    }

    private StartupPreferences getStartupPreferences() {
        List<StartupPreferences> allPreferences = startupPreferencesBox.getAll();
        StartupPreferences preferences = null;
        if (allPreferences.size() != 0) {
            preferences = allPreferences.get(0);
        }
        return preferences;
}

    private BoxStore getBoxStore() {
        return ((SpotifyNotificationsApplication) getApplication()).getBoxStore();
    }

}
