package servent.handler;

import app.AppConfig;
import servent.message.CheckingMessage;
import servent.message.ImpostorMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.util.MessageUtil;

public class CheckingHandler implements MessageHandler {

    private Message clientMessage;

    public CheckingHandler(Message clientMessage) {
        this.clientMessage = clientMessage;

    }

    @Override
    public void run() {

        if (clientMessage.getMessageType() == MessageType.CHECKING) {
            if (clientMessage.isIsItDead() && !AppConfig.deadNodes.contains(clientMessage.getSusPort())) {
                AppConfig.timestampedErrorPrint("SALJEM IMPOSTOR MESSAGE da ne radi U CHECKINGU ");

                AppConfig.timestampedErrorPrint(clientMessage.getSenderPort() + " " + clientMessage.getSusPort() + " " + clientMessage.getOriginalSenderPort());
                Message impostorMessage = new ImpostorMessage(AppConfig.myServentInfo.getListenerPort(),
                        clientMessage.getSenderPort(),
                        "checking",
                        clientMessage.getSusPort(),
                        clientMessage.getOriginalSenderPort()
                );
                MessageUtil.sendMessage(impostorMessage);
                AppConfig.deadNodes.add(clientMessage.getSusPort());
                AppConfig.countDown.set(0);
                AppConfig.sendPing.set(false);

            } else {
                if (AppConfig.recievedPingMap.get(clientMessage.getSusPort()) == null) {
                } else {
                    Long s = System.currentTimeMillis() - AppConfig.recievedPingTimer.get();

                    if (System.currentTimeMillis() - AppConfig.recievedPingTimer.get() > AppConfig.WEAK_TIMEOUT) {
                        AppConfig.timestampedErrorPrint("SALJEM CHECKING MESSAGE da ne radi U CHECKINGU " + clientMessage.getSenderPort());
                        Message checkingMessage = new CheckingMessage(MessageType.CHECKING, AppConfig.myServentInfo.getListenerPort(),
                                clientMessage.getSenderPort(), System.currentTimeMillis(), clientMessage.getSusPort(), true);
                        MessageUtil.sendMessage(checkingMessage);
                        AppConfig.timestampedErrorPrint("SALJEM CHECKING MESSAGE da ne radi U CHECKINGU POSLE" + clientMessage.getSenderPort());
                    }

                }

            }


        }


    }
}
