package com.app.miniIns.entities;

import java.net.URL;
import java.util.UUID;

public class ClientPhoto {

    private String username;
    private URL url;
    private UUID uuid;

    public ClientPhoto(String username, URL url, UUID uuid) {
        this.username = username;
        this.url = url;
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
