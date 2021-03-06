package com.app.miniIns.entities.server;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Photo implements  Comparable{

    // To avoid checking the existence before inserting to table
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Type(type="org.hibernate.type.UUIDCharType")
//    @Type(type="uuid-char")
    private UUID uuid;

    private String s3Key = UUID.randomUUID().toString();

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name="userId", referencedColumnName = "id")
    @NotNull
    private User user;

    @NotNull
    private String filename;

    @CreationTimestamp
    private ZonedDateTime createDateTime;

    @ManyToMany
    @JoinTable(name="photo_liked_by_user",
            joinColumns=@JoinColumn(name = "photo_id"),
            inverseJoinColumns=@JoinColumn(name = "user_id"))
    private List<User> likedBy;

    @OneToMany (
            mappedBy = "photo",
            cascade = CascadeType.ALL)
    private List<PhotoComment> comments = new ArrayList<>();


        public ZonedDateTime getCreateDateTime() {
        return createDateTime;
    }

    public List<User> getLikedBy() {
        return likedBy;
    }

    public List<PhotoComment> getComments() {
        return comments;
    }

    public void setLikedBy(List<User> likedBy) {
        this.likedBy = likedBy;
    }

//    public void setCreateDateTime(LocalDateTime createdDateTime) {
//        this.createDateTime = createdDateTime;
//    }


    public Photo() {
    }

    public Photo(UUID uuid, String s3Key, ZonedDateTime createDateTime) {
        this.uuid = uuid;
        this.s3Key = s3Key;
        this.createDateTime = createDateTime;
    }

    public Photo(UUID uuid){this.uuid = uuid;}

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
