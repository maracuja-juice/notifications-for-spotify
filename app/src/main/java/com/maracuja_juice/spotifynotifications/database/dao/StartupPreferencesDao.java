package com.maracuja_juice.spotifynotifications.database.dao;

import com.maracuja_juice.spotifynotifications.model.StartupPreferences;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

import static com.maracuja_juice.spotifynotifications.SpotifyNotificationsApplication.getBoxStore;

/**
 * Created by Maurice on 31.03.18.
 */

public class StartupPreferencesDao {
    private static Box<StartupPreferences> getStartupPreferencesBox() {
        BoxStore boxStore = getBoxStore();
        return boxStore.boxFor(StartupPreferences.class);
    }

    public static StartupPreferences getStartupPreferences() {
        // TODO: how to use runInTxAsync here?
        List<StartupPreferences> allPreferences = getStartupPreferencesBox().getAll();
        StartupPreferences preferences;
        if (allPreferences.size() != 0) {
            preferences = allPreferences.get(0);
        } else {
            preferences = new StartupPreferences();
        }
        return preferences;
    }

    public static void updateStartupPreferences(StartupPreferences startupPreferences) {
        getBoxStore().runInTxAsync(() -> {
            getStartupPreferencesBox().put(startupPreferences);
        }, null);
    }
}
