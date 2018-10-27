package com.maracuja_juice.spotifynotifications.helper;

import android.content.pm.PackageManager;

public class AppInstallationHelper {

    public static boolean isSpotifyInstalled(PackageManager packageManager) {
        boolean isSpotifyInstalled;
        try {
            packageManager.getPackageInfo("com.spotify.music", 0);
            isSpotifyInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            isSpotifyInstalled = false;
        }
        return isSpotifyInstalled;
    }
}
