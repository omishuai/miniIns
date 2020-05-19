package com.app.miniIns.entities;

import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Message implements Comparable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn (name="senderId", referencedColumnName = "id")
    private User sender;

    @ManyToOne
    @JoinColumn (name="receiverId", referencedColumnName = "id")
    private User receiver;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    private String text;


    public Message(){}

    public Message (User sender, User receiver, String text) {
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", createDateTime=" + createDateTime +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) return -1;
        if (o instanceof Message) {
            Message target = (Message) o;
            if (this.getCreateDateTime().isBefore((target.getCreateDateTime()))) return -1;
            return 1;
        }
        return -1;
    }

    public int getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public String getText() {
        return text;
    }
}
