package servent.handler;

import mutex.SuzukiKasami;
import servent.message.Message;
import servent.message.MessageType;

public class TokenHandler implements MessageHandler {

    private Message clientMessage;

    public TokenHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.TOKEN) {
      //      SuzukiKasami.receiveToken();
        }
    }
}
