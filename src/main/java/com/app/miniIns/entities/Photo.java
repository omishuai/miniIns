package com.app.miniIns.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table (name = "Photos")
public class Photo {
    @Id
    UUID id = UUID.randomUUID();

    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn (name = "userId", referencedColumnName = "id")
    @NotNull
    ServerUser user;

    @NotNull
    String filename;

    @NotNull
    String s3Bucket;

    public Photo(){}
    public Photo(ServerUser user, String s3Bucket, String filename) {
        this.user = user;
        this.s3Bucket = s3Bucket;
        this.filename = filename;
    }


    public String toString() {
        return String.format("{id: '%s', userId: %d, filename: '%s', s3_bucket: '%s'}", id, user.getId(), filename, s3Bucket);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ServerUser getUser() {
        return user;
    }

    public void setUser(ServerUser user) {
        this.user = user;
    }

    public String getS3_bucket() {
        return s3Bucket;
    }

    public void setS3_bucket(String s3_bucket) {
        this.s3Bucket = s3_bucket;
    }



}
