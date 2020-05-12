package com.app.miniIns.entities;


public class UserRelation {
    private ClientUser user1;
    private ClientUser user2;

    public UserRelation(ClientUser user1, ClientUser user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    public String toString() {
        return  String.format("{user1: %s, user2: %s}", user1, user2);
    }

    public ClientUser getUser1() {
        return user1;
    }

    public ClientUser getUser2() {
        return user2;
    }


}
