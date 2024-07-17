package servent.message;

import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;

public class ViewMessage extends BasicMessage{


    public ViewMessage(MessageType type, int senderPort, int receiverPort, CopyOnWriteArrayList<File> fileList,int viewPort, int originalSenderPort) {
        super(type, senderPort, receiverPort, fileList,viewPort, originalSenderPort);
    }


}
