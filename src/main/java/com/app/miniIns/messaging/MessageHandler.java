package com.app.miniIns.messaging;

import com.app.miniIns.entities.Message;
import com.app.miniIns.entities.User;
import com.app.miniIns.services.MessageService;
import com.app.miniIns.services.UserService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

@Component
public class MessageHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    HashMap<String, WebSocketSession> sessionsByclientName = new HashMap<>();

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        LOGGER.info("Received message: "+ message.getPayload());
        Map value = new Gson().fromJson(message.getPayload(), Map.class);
        String type = (String) value.get("type");


        if (type.equals("ack")) {
            double messageId = (Double)value.get("messageId");
            // remove from database;
            messageService.deleteById((int)messageId);

            LOGGER.info(messageId + "  IS deleted from db");
            return;
        }

        String sender = session.getPrincipal().getName();
        String receiver = (String) value.get("receiver");
        String text = (String) value.get("message");

        WebSocketSession webSocketSenderSession = sessionsByclientName.get(sender);
        if (webSocketSenderSession == null) return;

        Message msg = messageService.addMessage(new Message(
                userService.findByUsername(sender),
                userService.findByUsername(receiver),
                text));


        webSocketSenderSession.sendMessage(new TextMessage(
                String.format("{type: \"%s\", message: \"%s\", messageId: %d, sender: \"%s\", receiver: \"%s\"}",
                        "ack",
                        "has been Received on Server",
                        msg.getId(),
                        msg.getSender().getUsername(),
                        msg.getReceiver().getUsername()
                        ))
        );

        WebSocketSession webSocketReceiverSession = sessionsByclientName.get(receiver);

        if (webSocketReceiverSession != null) {
            webSocketReceiverSession.sendMessage(new TextMessage(
                    String.format("{type: \"%s\", message: \"%s\", messageId: %d, sender: \"%s\", receiver: \"%s\", time:\"%s\"}",
                    "message",
                    msg.getText(),
                    msg.getId(),
                    msg.getSender().getUsername(),
                    msg.getReceiver().getUsername(),
                    msg.getCreateDateTime()
            )));
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Session here refers to a connection to the user
        String username =   session.getPrincipal().getName();

        LOGGER.info("User " + username + " is Connecting to Server through " + session);

        sessionsByclientName.put(username, session);

        // send all the messages that are pending for current user
        User user = userService.findByUsername(username);
        LOGGER.info("" + user);

       List<Message> messages =  messageService.findByReceiverId(user.getId());
       for (Message message: messages) {

           // send to the user
           session.sendMessage(new TextMessage(
                   String.format("{type: \"%s\", message: \"%s\", messageId: %d, sender: \"%s\", receiver: \"%s\", time:\"%s\"}",
                           "message",
                           message.getText(),
                           message.getId(),
                           message.getSender().getUsername(),
                           message.getReceiver().getUsername(),
                           message.getCreateDateTime()
                           ))
           );
//           Thread.sleep(1000);
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
