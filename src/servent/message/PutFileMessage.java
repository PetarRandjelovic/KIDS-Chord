package servent.message;

import java.io.File;

public class PutFileMessage extends BasicMessage {


    public PutFileMessage(int senderPort, int receiverPort, int key, File file, String messageText){
        super(MessageType.PUT, senderPort, receiverPort,key ,file, messageText);
    }
}
