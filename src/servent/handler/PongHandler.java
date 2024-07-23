package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;

public class PongHandler implements MessageHandler {

    private Message clientMessage;

    public PongHandler(Message clientMessage) {
        this.clientMessage = clientMessage;

    }

    @Override
    public void run() {

        if (clientMessage.getMessageType() == MessageType.PONG) {

            if (AppConfig.myServentInfo.getListenerPort() == 1300) {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            AppConfig.recievedPong.set(true);
            AppConfig.sendPing.set(false);
            AppConfig.countDown.set(0);

        }

    }
}
