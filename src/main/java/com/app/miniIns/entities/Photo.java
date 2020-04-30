package com.app.miniIns.entities;

import org.hibernate.loader.plan.spi.QuerySpaceUidNotRegisteredException;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id = UUID.randomUUID();

    @ManyToOne
    @JoinColumn (name="userId", referencedColumnName = "id")
    @NotNull
    User user;

    @NotNull
    String filename;

    public Photo(){}
    public Photo(User user, String filename) {
        this.user = user;
        this.filename = filename;
    }


    public String toString() {
        return String.format("{id: '%s', userId: %d, filename: '%s'}", id, user.getId(), filename);
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



}
