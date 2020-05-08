package com.app.miniIns.entities;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
public class Photo implements  Comparable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID uuid = UUID.randomUUID();

    @ManyToOne
    @JoinColumn (name="userId", referencedColumnName = "id")
    @NotNull
    private User user;

    @NotNull
    private String filename;



    @OneToMany (
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<User> likedBy;

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public List<User> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(List<User> likedBy) {
        this.likedBy = likedBy;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    @CreationTimestamp
    private LocalDateTime createdDateTime;

    public Photo(){}

    public Photo(User user, String filename) {
        this.user = user;
        this.filename = filename;
    }


    public String toString() {
        return String.format("{id: %s, userId: %d, filename: '%s'}", uuid, user.getId(), filename);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID id) {
        this.uuid = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public int compareTo(Object o) {

        if (o == null) return -1;
        if (o instanceof Photo) {
            Photo target = (Photo) o;
            if (this.getCreatedDateTime().isAfter((target.getCreatedDateTime()))) return -1;
            return 1;
        }
        return 1;
    }

}
