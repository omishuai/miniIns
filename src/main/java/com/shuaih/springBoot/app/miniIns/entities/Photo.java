package com.shuaih.springBoot.app.miniIns.entities;


import javax.persistence.*;

@Entity
@Table(name = "Photo")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String filepath;
    private String url;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "uid")
    private User user;


    public Photo(String filepath, String url, User user) {
        this.filepath = filepath;
        this.url = url;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public String getFilepath() {
        return filepath;
    }

    public String getUrl() {
        return url;
    }

    public User getUser() {
        return user;
    }
}
