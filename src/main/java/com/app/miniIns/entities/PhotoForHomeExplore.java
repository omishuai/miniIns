package com.app.miniIns.entities;

import org.hibernate.annotations.CreationTimestamp;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.UUID;

public class PhotoForHomeExplore implements Comparable {
    private String username;
    private String s3Key;
    private URL s3Url;
    private UUID uuid;
    private LocalDateTime createDateTime;
    private long commentsCount;
    private long likedByCount;

    public PhotoForHomeExplore(String username, String s3Key, UUID uuid, LocalDateTime createDateTime, long commentsCount, long likedByCount) {
        this.username = username;
        this.s3Key = s3Key;
        this.uuid = uuid;
        this.createDateTime = createDateTime;
        this.commentsCount = commentsCount;
        this.likedByCount = likedByCount;
    }

    @Override
    public String toString() {
        return "PhotoForHome{" +
                "username='" + username + '\'' +
                ", s3Key='" + s3Key + '\'' +
                ", uuid=" + uuid +
                ", createDateTime=" + createDateTime +
                ", commentsCount=" + commentsCount +
                ", likedByCount=" + likedByCount +
                '}';
    }

    @Override
    public int compareTo(Object o) {

        if (o == null) return -1;
        if (o instanceof PhotoForHomeExplore) {
            PhotoForHomeExplore target = (PhotoForHomeExplore) o;
            if (this.getCreateDateTime().isAfter((target.getCreateDateTime()))) return -1;
            return 1;
        }
        return 1;
    }

    public URL getS3Url() {
        return s3Url;
    }

    public void setS3Url(URL s3Url) {
        this.s3Url = s3Url;
    }

    public long getCommentsCount() {
        return commentsCount;
    }

    public long getLikedByCount() {
        return likedByCount;
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
