package servent.message;

import app.ChordState;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A default message implementation. This should cover most situations.
 * If you want to add stuff, remember to think about the modificator methods.
 * If you don't override the modificators, you might drop stuff.
 *
 * @author bmilojkovic
 */
public class BasicMessage implements Message {

    private static final long serialVersionUID = -9075856313609777945L;
    private final MessageType type;
    private final int senderPort;
    private final int receiverPort;
    private String messageText;
    private long pingTime;
    private int susPort;
    private CopyOnWriteArrayList<File> listFiles;
    private int viewPort;
    private int originalSenderPort;
    private File file;
    private int key;
    private boolean isItDead;

    //This gives us a unique id - incremented in every natural constructor.
    private static AtomicInteger messageCounter = new AtomicInteger(0);
    private final int messageId;

    public BasicMessage(MessageType type, int senderPort, int receiverPort) {
        this.type = type;
        this.senderPort = senderPort;
        this.receiverPort = receiverPort;
        this.messageText = "";

        this.messageId = messageCounter.getAndIncrement();
    }

    //PUT i UPDATE
    public BasicMessage(MessageType type, int senderPort, int receiverPort, String messageText) {
        this.type = type;
        this.senderPort = senderPort;
        this.receiverPort = receiverPort;
        this.messageText = messageText;

        this.messageId = messageCounter.getAndIncrement();
    }

    //UPDATE
    public BasicMessage(MessageType type, int senderPort, int receiverPort, String messageText, int originalSenderPort) {
        this.type = type;
        this.senderPort = senderPort;
        this.receiverPort = receiverPort;
        this.messageText = messageText;
        this.originalSenderPort = originalSenderPort;
        this.messageId = messageCounter.getAndIncrement();
    }

    public BasicMessage(MessageType type, int senderPort, int receiverPort, String messageText, int susPort, int originalSenderPort) {
        this.type = type;
        this.senderPort = senderPort;
        this.receiverPort = receiverPort;
        this.messageText = messageText;
        this.susPort = susPort;
        this.originalSenderPort = originalSenderPort;
        this.messageId = messageCounter.getAndIncrement();
    }

    //createCopy message
    public BasicMessage(MessageType type, int senderPort, int receiverPort, File file, int originalSenderPort, String messageText) {
        this.type = type;
        this.senderPort = senderPort;
        this.receiverPort = receiverPort;
        this.file = file;
        this.originalSenderPort = originalSenderPort;
        this.messageText = messageText;
        this.messageId = messageCounter.getAndIncrement();
    }

    //PUTFILE
    public BasicMessage(MessageType type, int senderPort, int receiverPort, int key, File file, String messageText) {
        this.type = type;
        this.senderPort = senderPort;
        this.receiverPort = receiverPort;
        this.file = file;
        this.key = key;
        this.messageText = messageText;
        this.messageId = messageCounter.getAndIncrement();
    }


    public BasicMessage(MessageType type, int senderPort, int receiverPort, long pingTime) {
        this.type = type;
        this.senderPort = senderPort;
        this.receiverPort = receiverPort;
        this.messageText = "";
        this.pingTime = pingTime;
        this.messageId = messageCounter.getAndIncrement();
    }


    public BasicMessage(MessageType type, int senderPort, int receiverPort, long pingTime, int susPort) {
        this.type = type;
        this.senderPort = senderPort;
        this.receiverPort = receiverPort;
        this.messageText = "";
        this.pingTime = pingTime;
        this.messageId = messageCounter.getAndIncrement();
        this.susPort = susPort;
    }

    //CHECKING
    public BasicMessage(MessageType type, int senderPort, int receiverPort, long pingTime, int susPort, boolean isItDead) {
        this.type = type;
        this.senderPort = senderPort;
        this.receiverPort = receiverPort;
        this.messageText = "";
        this.pingTime = pingTime;
        this.messageId = messageCounter.getAndIncrement();
        this.susPort = susPort;
        this.isItDead = isItDead;
    }

    public BasicMessage(MessageType type, int senderPort, int receiverPort, CopyOnWriteArrayList<File> listFiles) {
        this.type = type;
        this.senderPort = senderPort;
        this.receiverPort = receiverPort;
        this.messageText = "";
        this.listFiles = listFiles;
        this.messageId = messageCounter.getAndIncrement();
    }

    public BasicMessage(MessageType type, int senderPort, int receiverPort, CopyOnWriteArrayList<File> listFiles, int viewPort, int originalSenderPort) {
        this.type = type;
        this.senderPort = senderPort;
        this.receiverPort = receiverPort;
        this.messageText = "";
        this.listFiles = listFiles;
        this.messageId = messageCounter.getAndIncrement();
        this.viewPort = viewPort;
        this.originalSenderPort = originalSenderPort;
    }

    @Override
    public MessageType getMessageType() {
        return type;
    }

    @Override
    public int getReceiverPort() {
        return receiverPort;
    }

    @Override
    public boolean isIsItDead() {
        return isItDead;
    }

    @Override
    public String getReceiverIpAddress() {
        return "localhost";
    }

    @Override
    public int getSenderPort() {
        return senderPort;
    }

    @Override
    public String getMessageText() {
        return messageText;
    }

    @Override
    public int getMessageId() {
        return messageId;
    }

    @Override
    public long getPingTime() {
        return pingTime;
    }

    @Override
    public int getSusPort() {
        return susPort;
    }

    @Override
    public CopyOnWriteArrayList<File> getNodesListFiles() {
        return listFiles;
    }

    @Override
    public int getViewPort() {
        return viewPort;
    }

    @Override
    public int getOriginalSenderPort() {
        return originalSenderPort;
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public int getKey() {
        return key;
    }

    /**
     * Comparing messages is based on their unique id and the original sender port.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BasicMessage) {
            BasicMessage other = (BasicMessage) obj;

            if (getMessageId() == other.getMessageId() &&
                    getSenderPort() == other.getSenderPort()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Hash needs to mirror equals, especially if we are gonna keep this object
     * in a set or a map. So, this is based on message id and original sender id also.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getMessageId(), getSenderPort());
    }

    /**
     * Returns the message in the format: <code>[sender_id|sender_port|message_id|text|type|receiver_port|receiver_id]</code>
     */
    @Override
    public String toString() {
        return "[" + ChordState.chordHash(getSenderPort()) + "|" + getSenderPort() + "|" + getMessageId() + "|" +
                getMessageText() + "|" + getMessageType() + "|" +
                getReceiverPort() + "|" + ChordState.chordHash(getReceiverPort()) + "]" + " " + isIsItDead();
    }

}
