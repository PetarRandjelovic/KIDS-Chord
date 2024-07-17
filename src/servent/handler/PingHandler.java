package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.PingMessage;
import servent.message.PongMessage;
import servent.message.util.MessageUtil;

public class PingHandler implements MessageHandler{

    private Message clientMessage;
    public PingHandler(Message clientMessage) {
        this.clientMessage = clientMessage;

    }

    @Override
    public void run() {

        if(clientMessage.getMessageType() == MessageType.PING) {

            if(AppConfig.myServentInfo.getListenerPort()== 1300){
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            AppConfig.recievedPing.set(true);
            AppConfig.recievedPingMap.put(clientMessage.getSenderPort(),true);

            long s=System.currentTimeMillis();
            AppConfig.recievedPingTimer.set(System.currentTimeMillis());

            Message pongMsgSuc = new PongMessage(MessageType.PONG, AppConfig.myServentInfo.getListenerPort(),
                    clientMessage.getSenderPort(),clientMessage.getPingTime());
            MessageUtil.sendMessage(pongMsgSuc);



            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            AppConfig.recievedPing.set(false);
            AppConfig.recievedPingMap.put(clientMessage.getSenderPort(),true);

        }
    }
}
