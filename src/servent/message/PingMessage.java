package servent.message;

public class PingMessage extends BasicMessage{


    public PingMessage(MessageType type, int senderPort, int receiverPort,long pingTime) {
        super(type, senderPort, receiverPort, pingTime);
    }


}
