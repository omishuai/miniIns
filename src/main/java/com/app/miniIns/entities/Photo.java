package com.app.miniIns.entities;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Photo implements  Comparable{

    // To avoid checking the existence before inserting to table
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID uuid;

    private String s3Key = UUID.randomUUID().toString();

    @ManyToOne
    @JoinColumn (name="userId", referencedColumnName = "id")
    @NotNull
    private User user;

    @NotNull
    private String filename;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @OneToMany (
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<User> likedBy;

    @OneToMany (
            mappedBy = "photo",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PhotoComment> comments = new ArrayList<>();


    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public List<User> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(List<User> likedBy) {
        this.likedBy = likedBy;
    }

//    public void setCreateDateTime(LocalDateTime createdDateTime) {
//        this.createDateTime = createdDateTime;
//    }



    public Photo(){}

    public Photo(User user, String filename) {
        this.user = user;
        this.filename = filename;
    }

    @Override
    public int compareTo(Object o) {

        if (o == null) return -1;
        if (o instanceof Photo) {
            Photo target = (Photo) o;
            if (this.getCreateDateTime().isAfter((target.getCreateDateTime()))) return -1;
            return 1;
        }
        return 1;
    }
    public String toString() {
        return String.format("{uuid: %s, userId: %d, filename: '%s'}", uuid, user.getId(), filename);
    }

    public String getS3Key() {
        return s3Key;
    }

    public UUID getUuid() {
        return uuid;
    }

    public User getUser() {
        return user;
    }

}
