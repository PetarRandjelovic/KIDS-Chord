package servent.message;

public class ImpostorMessage extends BasicMessage{

    public ImpostorMessage(int senderPort, int receiverPort, String text, int susPort, int originalSenderPort) {
        super(MessageType.IMPOSTOR, senderPort, receiverPort, text, susPort, originalSenderPort);
    }
}
