package com.app.miniIns.messaging;

import com.app.miniIns.entities.Message;
import com.app.miniIns.services.MessageService;
import com.app.miniIns.services.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;



public class MessageHandler extends TextWebSocketHandler {

    HashMap<String, WebSocketSession> sessionsByclientName = new HashMap<>();

    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        Map value = new Gson().fromJson(message.getPayload(), Map.class);
        String type = (String) value.get("type");
        if (type.equals("ack")) {
            int messageId = (int)value.get("messageId");
            // remove from database;
            messageService.deleteById(messageId);
            return;
        }

        String sender = session.getPrincipal().getName();
        String receiver = (String) value.get("receiver");
        String text = (String) value.get("text");

        messageService.addMessage(new Message(
                userService.findByUsername(sender),
                userService.findByUsername(receiver),
                text));

        WebSocketSession webSocketSenderSession = sessionsByclientName.get(sender);
        webSocketSenderSession.sendMessage(new TextMessage("Message has been Received on Server"));

        WebSocketSession webSocketReceiverSession = sessionsByclientName.get(receiver);

        if (webSocketReceiverSession != null) {
            webSocketReceiverSession.sendMessage(new TextMessage(sender + value.get("text")));
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Session here refers to a connection to the user
        String username =  session.getPrincipal().getName();
        sessionsByclientName.put(username, session);

        // send all the messages that are pending for current user
       List<Message> messages =  messageService.findByReceiverId(userService.findByUsername(username).getId());
       for (Message message: messages) {

           // send to the user
           session.sendMessage(new TextMessage(message.getText()));
       }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionsByclientName.remove(session.getPrincipal().getName());
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }
}
