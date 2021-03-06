package com.app.miniIns.messaging;

import com.app.miniIns.entities.server.Message;
import com.app.miniIns.entities.server.User;
import com.app.miniIns.services.services.MessageService;
import com.app.miniIns.services.services.UserService;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
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

    private Map<String, WebSocketSession> sessionsByclientName = new ConcurrentHashMap<>();

    private String RECIPIENT_NOT_FOUND =  "Recipient %s Is Not Found";
    private String MESSAGE_TYPE_NOT_FOUND =  "Message Type %s Is Not Found";
    private String CANNOT_SEND_TO_SELF = "Sender %s Cannot Send to Recipient %s";
    private String EMPTY_TEXT = "Invalid Text From User. Text: %s";
    private String INVALID_MESSAGE_ID = "Invalid Message ID %s";

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{

        String sender = session.getPrincipal().getName();
        WebSocketSession webSocketSenderSession = sessionsByclientName.get(sender);
        if (webSocketSenderSession == null) throw new Exception("WebSocket For " + sender + " Is Not Connected");

        try {
            Map value = new Gson().fromJson(message.getPayload(), Map.class);
            String type = (String) value.get("type");
            if (type.equals("ack")) {
                processAckMessage(sender, value);
            } else if (type.equals("message")) {
                processSendMessage(sender, webSocketSenderSession, value);
            } else {
                throw new Exception(String.format(MESSAGE_TYPE_NOT_FOUND, type));
            }
        } catch (Exception e) {
            webSocketSenderSession.sendMessage(new TextMessage(String.format("{type: \"%s\", message: \"%s\"}", "error", e.getMessage())));
        }
    }

    private void processSendMessage(String sender, WebSocketSession session, Map value) throws Exception{

        String receiver = (String) value.get("receiver");
        String text = (String) value.get("message");

        if (StringUtils.isEmpty(text)) {
            throw new Exception (String.format(EMPTY_TEXT, text));
        } else if (receiver.equals(sender)) {
            throw new Exception(String.format(CANNOT_SEND_TO_SELF, sender, receiver));
        }
        User receiverUser =  getRecipient(receiver);
        Message msg = messageService.addMessage(new Message(
                userService.findByUsername(sender),
                receiverUser,
                text));

        session.sendMessage(new TextMessage(String.format("{type: \"%s\"}", "ack")));

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

    private User getRecipient(String username) throws Exception {
        if (!StringUtils.isEmpty(username))  {
           User user = userService.findByUsername(username);
           if (user != null) return user;
        }
        throw new Exception(String.format(RECIPIENT_NOT_FOUND, username));
    }

    private void processAckMessage(String sender, Map value) throws Exception{

        if (value.get("messageId") == null) throw new Exception(String.format(INVALID_MESSAGE_ID, value.get("messageId")));

        int messageId = ((Double) value.get("messageId")).intValue();
        Message msg = messageService.findById(messageId);
        if (msg == null || !msg.getReceiver().getUsername().equals(sender)) throw new Exception(String.format(INVALID_MESSAGE_ID, messageId));

        messageService.deleteById(messageId);
        LOGGER.info("Message " + messageId + " IS deleted from db");
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Session here refers to a connection to the user
        String username =  session.getPrincipal().getName();
        WebSocketSession oldSession = sessionsByclientName.put(username, session);
        if (oldSession != null) {
            oldSession.close();
        }

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
       }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionsByclientName.remove(session.getPrincipal().getName(), session);
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
