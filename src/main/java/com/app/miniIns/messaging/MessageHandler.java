package com.app.miniIns.messaging;

import com.app.miniIns.entities.Message;
import com.app.miniIns.entities.User;
import com.app.miniIns.exceptions.NoTextContentException;
import com.app.miniIns.exceptions.RecipientNotFoundException;
import com.app.miniIns.exceptions.SendToSenderException;
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
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MessageHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    Map<String, WebSocketSession> sessionsByclientName = new ConcurrentHashMap<>();

    private String RECIPIENT_NOT_FOUND =  "Recipient %s Is Not Found";
    private String MESSAGE_TYPE_NOT_FOUND =  "Message Type %s Is Not Found";
    private String CANNOT_SEND_TO_SELF = "Sender %s Cannot Send to Recipient %s";
    private String EMPTY_TEXT = "Invalid Text From User. Text: %s";
    private String INVALID_MESSAGE_ID = "Invalid Message ID %s";
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String errorMessage = "";
        Map value = null;
        try {
            value = new Gson().fromJson(message.getPayload(), Map.class);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }

        String sender = session.getPrincipal().getName();
        WebSocketSession webSocketSenderSession = sessionsByclientName.get(sender);

        if (value == null) {
            webSocketSenderSession.sendMessage(new TextMessage(String.format("{type: \"%s\", message: \"%s\"}", "error", errorMessage)));
            return;
        }

        String type = (String) value.get("type");
        String receiver = (String) value.get("receiver");
        String text = (String) value.get("message");

        if (type == null || (!type.equals("ack") && !type.equals("message"))) errorMessage = String.format(MESSAGE_TYPE_NOT_FOUND, type);
        else if (type.equals("ack")) {
            if (value.get("messageId") == null) errorMessage = String.format(INVALID_MESSAGE_ID, value.get("messageId"));
            else {
                double messageId = (Double) value.get("messageId");
                if (messageService.findById((int) messageId) == null) errorMessage = String.format(INVALID_MESSAGE_ID, (int)messageId);
            }
        }
        else if (receiver == null || receiver.equals("") || userService.findByUsername(receiver) == null) errorMessage = String.format(RECIPIENT_NOT_FOUND, receiver);
        else if (receiver.equals(sender)) errorMessage = String.format(CANNOT_SEND_TO_SELF, sender, receiver);
        else if (text == null || text.equals("")) errorMessage = String.format(EMPTY_TEXT, text);


        if (!errorMessage.equals("")) {
            webSocketSenderSession.sendMessage(new TextMessage(String.format("{type: \"%s\", message: \"%s\"}", "error", errorMessage)));
            return;
        }

        if (type.equals("ack")) {
            double messageId = (Double) value.get("messageId");
            messageService.deleteById((int)messageId);
            LOGGER.info(messageId + "  IS deleted from db");
            return;
        }

        Message msg = messageService.addMessage(new Message(
                userService.findByUsername(sender),
                userService.findByUsername(receiver),
                text));

        webSocketSenderSession.sendMessage(new TextMessage(String.format("{type: \"%s\"}", "ack")));

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
        if (sessionsByclientName.containsKey(username)) {
            sessionsByclientName.get(username).close();
        }
        sessionsByclientName.put(username, session);

        // send all the messages that are pending for current user
        User user = userService.findByUsername(username);
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
