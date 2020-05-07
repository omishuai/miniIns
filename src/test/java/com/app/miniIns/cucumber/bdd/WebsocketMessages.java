package com.app.miniIns.cucumber.bdd;

import java.util.Hashtable;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class WebsocketMessages {
    Map<String, Semaphore> semaphoreMap;

    private Map<String, ConcurrentLinkedQueue<String>> queueMap;
    public WebsocketMessages () {
        queueMap = new ConcurrentHashMap<>();
        semaphoreMap = new Hashtable<>();
    }

    public String pollMessage(String websocket) throws InterruptedException {
        if (semaphoreMap.get(websocket).tryAcquire(2L, TimeUnit.SECONDS)) {
            Queue<String> messages = queueMap.get(websocket);
            return messages.poll();
        }
        return "";
    }

    public void addMessage(String websocket, String message) {
        Queue<String> messages = queueMap.computeIfAbsent(websocket, key -> new ConcurrentLinkedQueue<String>());
        messages.offer(message);
        Semaphore sm = semaphoreMap.get(websocket);
        sm.release();
    };

    public void openSocket(String websocket) {
        semaphoreMap.put(websocket,new Semaphore(0));
    }

    public void clear(){
        queueMap.clear();
        semaphoreMap.clear();
    }
}
