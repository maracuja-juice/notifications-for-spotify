package com.maracuja_juice.spotifynotifications.database.dao;

import android.util.Log;

import com.maracuja_juice.spotifynotifications.model.MyAlbum;
import com.maracuja_juice.spotifynotifications.model.MyAlbum_;

import org.joda.time.LocalDate;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidScheduler;
import io.objectbox.reactive.DataObserver;
import io.objectbox.reactive.DataSubscription;
import kaaes.spotify.webapi.android.models.Album;

import static com.maracuja_juice.spotifynotifications.SpotifyNotificationsApplication.getBoxStore;
import static com.maracuja_juice.spotifynotifications.helper.AlbumListComparer.getNewAlbums;
import static com.maracuja_juice.spotifynotifications.helper.AlbumToMyAlbumConverter.convertAlbumsToMyAlbums;
import static com.maracuja_juice.spotifynotifications.helper.AlbumToMyAlbumConverter.convertMyAlbumsToAlbums;

/**
 * Created by Maurice on 31.03.18.
 */

public class MyAlbumDao {

    private static final String LOG_TAG = MyAlbumDao.class.getName();

    private static Box<MyAlbum> getMyAlbumBox() {
        BoxStore boxStore = getBoxStore();
        return boxStore.boxFor(MyAlbum.class);
    }

    public static DataSubscription subscribeToMyAlbumList(DataObserver<List<MyAlbum>> observer) {
        return getMyAlbumBox().query()
                .greater(MyAlbum_.releaseDate, LocalDate.now().minusMonths(3).toString())
                .build().subscribe().on(AndroidScheduler.mainThread()).observer(observer);
    }

    public static void mergeAndSaveAlbums(List<Album> albums) {
        getBoxStore().runInTxAsync(() -> {
                    Box<MyAlbum> albumBox = getMyAlbumBox();

                    List<MyAlbum> savedMyAlbums = albumBox.getAll();
                    List<Album> savedAlbums = convertMyAlbumsToAlbums(savedMyAlbums);

                    List<Album> newAlbums = getNewAlbums(albums, savedAlbums);
                    Log.d(LOG_TAG, "there are " + newAlbums.size() + " new albums");
                    if (newAlbums.size() > 0) {
                        List<MyAlbum> newMyAlbums = convertAlbumsToMyAlbums(newAlbums);
                        albumBox.put(newMyAlbums);
                    }
                }, null
        );
    }
}