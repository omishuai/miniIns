package com.app.miniIns.services;

import com.app.miniIns.entities.Message;
import com.app.miniIns.entities.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Service
public class MessageService {


    @Autowired
    private MessageRepository messageRepository;


    public MessageRepository getMessageRepository() {
        return messageRepository;
    }

    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> findAll() {
        List<Message> messages = new ArrayList<>();
        Iterator<Message> itr = messageRepository.findAll().iterator();
        while (itr.hasNext()) messages.add(itr.next());
        return messages;
    }


    public List<Message> findByReceiverId(int receiverId) {
        List<Message> messages = messageRepository.findByReceiverId(receiverId);

        // Messages are all sorted in ascending order based on created time
        Collections.sort(messages);
        return messages;
    }

    public void delete(Message message) {
        messageRepository.delete(message);
    }

    public void deleteById(int messageId) {
        messageRepository.deleteById(messageId);
    }


    public Message addMessage(Message message){
        return messageRepository.save(message);
    }



}
