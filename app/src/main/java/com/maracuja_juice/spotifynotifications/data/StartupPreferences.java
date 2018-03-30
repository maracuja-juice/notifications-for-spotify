package com.maracuja_juice.spotifynotifications.data;

import com.maracuja_juice.spotifynotifications.data.converter.LocalDateConverter;
import com.maracuja_juice.spotifynotifications.data.converter.LocalDateTimeConverter;

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

    private String token;

    @Convert(converter = LocalDateConverter.class, dbType = String.class)
    private LocalDate lastDownload;

    public StartupPreferences() {
    }

    public StartupPreferences(Long id, LocalDateTime tokenExpiration, String token, LocalDate lastDownload) {
        this.id = id;
        this.tokenExpiration = tokenExpiration;
        this.token = token;
        this.lastDownload = lastDownload;
    }

    public boolean needToDownload() {
        // TODO: mock this datetime or something? Or somehow else make it adjustable for tests
        return needToDownload(LocalDate.now());
    }

    public boolean needToDownload(LocalDate threshold) {
        return lastDownload == null || threshold.isAfter(lastDownload);
    }

    public boolean needToLogin() {
        // TODO: mock this datetime or something? Or somehow else make it adjustable for tests
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDate getLastDownload() {
        return lastDownload;
    }

    public void setLastDownload(LocalDate lastDownload) {
        this.lastDownload = lastDownload;
    }
}
