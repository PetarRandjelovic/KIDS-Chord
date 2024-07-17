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

            //    AppConfig.timestampedErrorPrint((AppConfig.recievedPingMap+" "+clientMessage.getSusPort()+" "+AppConfig.recievedPingMap.get(clientMessage.getSusPort())));
            if (clientMessage.isIsItDead() && !AppConfig.deadNodes.contains(clientMessage.getSusPort())) {
                AppConfig.timestampedErrorPrint("SALJEM IMPOSTOR MESSAGE da ne radi U CHECKINGU ");

                AppConfig.timestampedErrorPrint(  clientMessage.getSenderPort()+" "+clientMessage.getSusPort()+" "+clientMessage.getOriginalSenderPort());
                Message impostorMessage = new ImpostorMessage(AppConfig.myServentInfo.getListenerPort(),
                        clientMessage.getSenderPort(),
                        "mrs",
                       clientMessage.getSusPort(),
                        clientMessage.getOriginalSenderPort()
                );
                MessageUtil.sendMessage(impostorMessage);
                AppConfig.deadNodes.add(clientMessage.getSusPort());
                AppConfig.countDown.set(0);
                AppConfig.sendPing.set(false);

            } else {
                if (AppConfig.recievedPingMap.get(clientMessage.getSusPort()) == null) {
                    //   AppConfig.timestampedErrorPrint("NEMA U MAPU");
                } else {
                    Long s = System.currentTimeMillis() - AppConfig.recievedPingTimer.get();

                    if (System.currentTimeMillis() - AppConfig.recievedPingTimer.get() > AppConfig.WEAK_TIMEOUT) {
                        AppConfig.timestampedErrorPrint("VREMEeee JE " + s);
                        AppConfig.timestampedErrorPrint("SALJEM CHECKING MESSAGE da ne radi U CHECKINGU " + clientMessage.getSenderPort());
                        Message checkingMessage = new CheckingMessage(MessageType.CHECKING, AppConfig.myServentInfo.getListenerPort(),
                                clientMessage.getSenderPort(), System.currentTimeMillis(), clientMessage.getSusPort(),true);
                        MessageUtil.sendMessage(checkingMessage);
                        AppConfig.timestampedErrorPrint("WTF VISE"+checkingMessage.getSenderPort()+" "+checkingMessage.getReceiverPort()+" "+checkingMessage.getSusPort());
                        AppConfig.timestampedErrorPrint("SALJEM CHECKING MESSAGE da ne radi U CHECKINGU POSLE" + clientMessage.getSenderPort());

                    } else {
                  //      AppConfig.timestampedErrorPrint("MA OKEJ JE KOJI TI JE " + clientMessage.getSusPort() + " ");
                    }

                }

                if (AppConfig.recievedPing.get()) {
                    long m = System.currentTimeMillis() - AppConfig.recievedPingTimer.get();
//                AppConfig.timestampedErrorPrint("DA NESTO NE VALJA BURAZ sa "+clientMessage.getSusPort() +" " +m);

                }
            }



        }


    }
}
