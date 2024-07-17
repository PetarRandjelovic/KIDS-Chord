package servent.message;

public class TokenMessage extends BasicMessage{


    public TokenMessage(int senderPort, int receiverPort) {
        super(MessageType.TOKEN, senderPort, receiverPort);
    }


}
