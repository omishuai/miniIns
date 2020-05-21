package com.app.miniIns.entities.server;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Table (name = "commentsOnPhoto")
public class PhotoComment  implements  Comparable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String text;

    @CreationTimestamp
    private ZonedDateTime createDateTime;

    private String fromUser;

    @ManyToOne
    @JoinColumn (name="photoId", referencedColumnName = "uuid")
    @NotNull
    private Photo photo;

    @Column(nullable = true)
    private int toId;

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public PhotoComment(){}

    public PhotoComment(String text, String from, int toId) {
        this.text = text;
        this.fromUser = from;
        this.toId = toId;
    }

    public PhotoComment(String text, String from) {
        this.text = text;
        this.fromUser = from;
    }


    @Override
    public int compareTo(Object o) {
        if (o == null) return -1;
        if (o instanceof PhotoComment) {
            PhotoComment target = (PhotoComment) o;
            if (this.getCreateDateTime().isBefore((target.getCreateDateTime()))) return -1;
            return 1;
        }
        return -1;
    }

    @Override
    public String toString() {
        return "PhotoComment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", createDateTime=" + createDateTime +
                ", from='" + fromUser + '\'' +
                ", photo=" + photo +
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

    public int getToId() {
        return toId;
    }



}
