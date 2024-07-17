package mutex;

import app.AppConfig;
import app.ChordState;
import servent.message.Message;
import servent.message.TokenMessage;
import servent.message.util.MessageUtil;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.LinkedList;
import java.util.Arrays;

public class SuzukiKasami {

    private static AtomicBoolean tokenActive = new AtomicBoolean(false);
    private static AtomicBoolean lockActive = new AtomicBoolean(false);
    private Queue<Integer> Q; // Queue of requests
    private int[] requestNumbers; // Request numbers
    private int[] lastRequestNumbers; // Last executed requests

    public SuzukiKasami() {
        this.requestNumbers = new int[ChordState.CHORD_SIZE];
        this.lastRequestNumbers = new int[ChordState.CHORD_SIZE];
        Arrays.fill(requestNumbers, 0);
        Arrays.fill(lastRequestNumbers, 0);
        this.Q = new LinkedList<>();
    }

    public synchronized void requestCriticalSection() {
        int myId = AppConfig.myServentInfo.getChordId();
        requestNumbers[myId]++;
        broadcastRequest(myId, requestNumbers[myId]);
    }

    private void broadcastRequest(int siteId, int sn) {
        System.out.println("Site " + siteId + " broadcasts REQUEST(" + siteId + ", " + sn + ")");
        for (int j = 0; j < ChordState.CHORD_SIZE; j++) {
            if (j != siteId) {
                sendMessage(j, "REQUEST," + siteId + "," + sn);
            }
        }
    }

    private void sendMessage(int toSiteId, String message) {
        Message requestMessage = new TokenMessage(AppConfig.myServentInfo.getListenerPort(), toSiteId);
        MessageUtil.sendMessage(requestMessage);
    }

    public synchronized void receiveRequest(int fromSiteId, int requestingSiteId, int sn) {
        requestNumbers[requestingSiteId] = Math.max(requestNumbers[requestingSiteId], sn);
        System.out.println("Site " + fromSiteId + " received REQUEST(" + requestingSiteId + ", " + sn + ")");
        int myId = AppConfig.myServentInfo.getChordId();
        if (tokenActive.get() && requestNumbers[requestingSiteId] == lastRequestNumbers[requestingSiteId] + 1) {
            sendToken(requestingSiteId);
        }
    }

    private void sendToken(int toSiteId) {
        System.out.println("Site " + AppConfig.myServentInfo.getChordId() + " sends TOKEN to Site " + toSiteId);
        tokenActive.set(false);
        Message tokenMessage = new TokenMessage(AppConfig.myServentInfo.getListenerPort(), toSiteId);
        MessageUtil.sendMessage(tokenMessage);
    }

    public synchronized void receiveToken() {
        System.out.println("Site " + AppConfig.myServentInfo.getChordId() + " received TOKEN");
        tokenActive.set(true);
        executeCriticalSection();
    }

    private void executeCriticalSection() {
        System.out.println("Site " + AppConfig.myServentInfo.getChordId() + " is entering the critical section.");
        try {
            Thread.sleep(1000); // Simulate the critical section with sleep
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Site " + AppConfig.myServentInfo.getChordId() + " is leaving the critical section.");
        releaseCriticalSection();
    }

    private void releaseCriticalSection() {
        int myId = AppConfig.myServentInfo.getChordId();
        lastRequestNumbers[myId] = requestNumbers[myId];
        for (int j = 0; j < ChordState.CHORD_SIZE; j++) {
            if (j != myId && requestNumbers[j] == lastRequestNumbers[j] + 1 && !Q.contains(j)) {
                Q.add(j);
            }
        }
        if (!Q.isEmpty()) {
            int nextSite = Q.poll();
            sendToken(nextSite);
        }
    }
}
