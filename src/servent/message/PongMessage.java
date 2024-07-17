package servent.message;

public class PongMessage extends BasicMessage{


    public PongMessage(MessageType type, int senderPort, int receiverPort,long pingTime) {
        super(type, senderPort, receiverPort, pingTime);
    }

}
