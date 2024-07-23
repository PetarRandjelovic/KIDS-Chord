package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;

public class PutHandler implements MessageHandler {

    private Message clientMessage;

    public PutHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.PUT) {
            //AppConfig.timestampedErrorPrint("LOGIKA ZA OBICAN FILE");
            if (clientMessage.getFile() == null) {
                String[] splitText = clientMessage.getMessageText().split(":");
                if (splitText.length == 2) {
                    int key = 0;
                    int value = 0;

                    try {
                        key = Integer.parseInt(splitText[0]);
                        value = Integer.parseInt(splitText[1]);

                        AppConfig.chordState.putValue(key, value);
                    } catch (NumberFormatException e) {
                        AppConfig.timestampedErrorPrint("Got put message with bad text: " + clientMessage.getMessageText());
                    }
                } else {
                    AppConfig.timestampedErrorPrint("Got put message with bad text: " + clientMessage.getMessageText());
                }
            } else {
                AppConfig.chordState.putValueFile(clientMessage.getKey(), clientMessage.getFile(), clientMessage.getMessageText());
            }

        } else {
            AppConfig.timestampedErrorPrint("Put handler got a message that is not PUT");
        }

    }

}
