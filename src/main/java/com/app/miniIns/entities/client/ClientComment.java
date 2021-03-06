package com.app.miniIns.entities.client;


import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

public class ClientComment {

    private int id;

    private String text;

    private ZonedDateTime createDateTime;

    private String fromUser;

    private UUID photoId;

    private int toId;

    public ClientComment(int id, String text, ZonedDateTime createDateTime, String fromUser, UUID photoId, int toId) {
        this.id = id;
        this.text = text;
        this.createDateTime = createDateTime;
        this.fromUser = fromUser;
        this.photoId = photoId;
        this.toId = toId;
    }

    @Override
    public String toString() {
        return "ClientComment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", createDateTime=" + createDateTime +
                ", fromUser='" + fromUser + '\'' +
                ", photoId=" + photoId +
                ", toId=" + toId +
                '}';
    }


    public int getId() {
        return id;
    }


    public String getText() {
        return text;
    }


    public ZonedDateTime getCreateDateTime() {
        return createDateTime;
    }

    public String getFromUser() {
        return fromUser;
    }

    public UUID getPhotoId() {
        return photoId;
    }

    public int getToId() {
        return toId;
    }
}
