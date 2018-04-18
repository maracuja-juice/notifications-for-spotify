package com.maracuja_juice.spotifynotifications.model;

import com.maracuja_juice.spotifynotifications.database.converter.LocalDateConverter;
import com.maracuja_juice.spotifynotifications.database.converter.LocalDateTimeConverter;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by Maurice on 29.03.18.
 */

// TODO I need a better name for this!
@Entity
public class StartupPreferences {
    @Id
    private Long id;

    @Convert(converter = LocalDateTimeConverter.class, dbType = String.class)
    private LocalDateTime tokenExpiration;

    private String currentAccessToken;
    private String refreshToken;

    @Convert(converter = LocalDateConverter.class, dbType = String.class)
    private LocalDate lastDownload;

    public StartupPreferences() {
    }

    public StartupPreferences(Long id, LocalDateTime tokenExpiration, String currentAccessToken, String refreshToken, LocalDate lastDownload) {
        this.id = id;
        this.tokenExpiration = tokenExpiration;
        this.currentAccessToken = currentAccessToken;
        this.refreshToken = refreshToken;
        this.lastDownload = lastDownload;
    }

    public boolean needToDownload() {
        return lastDownload == null || LocalDate.now().isAfter(lastDownload);
    }

    public boolean needToAuthorize() {
        return refreshToken == null; // TODO: re authorization won't happen with this code
    }

    public boolean needToLogin() {
        return tokenExpiration == null || LocalDateTime.now().isAfter(tokenExpiration);
    }

    // Get and Set
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTokenExpiration() {
        return tokenExpiration;
    }

    public void setTokenExpiration(LocalDateTime tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }

    public String getCurrentAccessToken() {
        return currentAccessToken;
    }

    public void setCurrentAccessToken(String currentAccessToken) {
        this.currentAccessToken = currentAccessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public LocalDate getLastDownload() {
        return lastDownload;
    }

    public void setLastDownload(LocalDate lastDownload) {
        this.lastDownload = lastDownload;
    }

}
