package servent.message;

public class CheckingMessage extends BasicMessage{


    public CheckingMessage(MessageType type, int senderPort, int receiverPort,long pingTime, int susPort, boolean isItDead) {
        super(type, senderPort, receiverPort, pingTime, susPort, isItDead);
    }
}
