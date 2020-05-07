package com.app.miniIns.messaging;

import com.app.miniIns.entities.Message;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WebsocketMessages {

    private Map<String, ConcurrentLinkedQueue<String>> queueMap;
    public WebsocketMessages () {
        queueMap = new ConcurrentHashMap<>();
    }

    public String pollMessage(String websocket) throws InterruptedException {
        Thread.sleep(2000);
        Queue<String> messages = queueMap.get(websocket);
        if (messages != null && messages.size() > 0)
            return messages.poll();
        return "";
    }

    public void addMessage(String websocket, String message) {
        Queue<String> messages = queueMap.computeIfAbsent(websocket, key -> new ConcurrentLinkedQueue<String>());
        messages.add(message);
    };

    public void clear(){
        queueMap.clear();
    }
}
