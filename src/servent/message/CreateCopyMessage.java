package servent.message;

import java.io.File;

public class CreateCopyMessage extends BasicMessage{

    public CreateCopyMessage(int senderPort, int receiverPort, File file, int originalSenderPort, String messageText) {
        super(MessageType.CREATE_COPY, senderPort, receiverPort, file, originalSenderPort, messageText);
    }

}
