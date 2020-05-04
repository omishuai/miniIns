package com.app.miniIns.services;

import com.app.miniIns.entities.Message;
import com.app.miniIns.entities.Photo;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Service
public class MessageService {

    private MessageRepository messageRepository;


    public MessageRepository getMessageRepository() {
        return messageRepository;
    }

    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
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
