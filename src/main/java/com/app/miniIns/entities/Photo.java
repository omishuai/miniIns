package com.app.miniIns.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
public class Photo {
    @Id
    UUID id = UUID.randomUUID();

    @ManyToOne
    @JoinColumn (name="userId", referencedColumnName = "id")
    @NotNull
    User user;

    @NotNull
    String filename;

    @NotNull
    String s3Bucket;

    public Photo(){}
    public Photo(User user, String s3Bucket, String filename) {
        this.user = user;
        this.s3Bucket = s3Bucket;
        this.filename = filename;
    }


    public String toString() {
        return String.format("{id: '%s', userId: %d, filename: '%s', s3Bucket: '%s'}", id, user.getId(), filename, s3Bucket);
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getS3Bucket() {
        return s3Bucket;
    }

    public void setS3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
    }



}
