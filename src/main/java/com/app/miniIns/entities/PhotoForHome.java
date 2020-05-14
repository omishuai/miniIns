package com.app.miniIns.entities;

import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

public class PhotoForHome implements Comparable {
    private String username;
    private String s3Key;
    private UUID uuid;
    private LocalDateTime createDateTime;

    public PhotoForHome(String username, String s3Key, UUID uuid, LocalDateTime createDateTime) {
        this.username = username;
        this.s3Key = s3Key;
        this.uuid = uuid;
        this.createDateTime = createDateTime;
    }

    @Override
    public String toString() {
        return "PhotoForHome{" +
                "username='" + username + '\'' +
                ", s3Key='" + s3Key + '\'' +
                ", uuid=" + uuid +
                ", createDateTime=" + createDateTime +
                '}';
    }

    @Override
    public int compareTo(Object o) {

        if (o == null) return -1;
        if (o instanceof PhotoForHome) {
            PhotoForHome target = (PhotoForHome) o;
            if (this.getCreateDateTime().isAfter((target.getCreateDateTime()))) return -1;
            return 1;
        }
        return 1;
    }


    public String getUsername() {
        return username;
    }

    public String getS3Key() {
        return s3Key;
    }

    public UUID getUuid() {
        return uuid;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }
}
