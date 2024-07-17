package servent.handler;

import app.AppConfig;
import servent.message.ImpostorMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.util.MessageUtil;

public class ImpostorHandler implements MessageHandler{

    private Message clientMessage;

    public ImpostorHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }
    @Override
    public void run() {

        if(clientMessage.getMessageType() == MessageType.IMPOSTOR) {
       //    AppConfig.timestampedErrorPrint("IMPOSTOR MESSAGE RECEIVED");
            if(clientMessage.getOriginalSenderPort()!= AppConfig.myServentInfo.getListenerPort() && !AppConfig.deadNodes.contains(clientMessage.getSusPort())) {
                AppConfig.timestampedErrorPrint("BRISEMO");

                AppConfig.chordState.removeNodes(clientMessage.getSusPort());

//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }

                Message impostorMessage = new ImpostorMessage(AppConfig.myServentInfo.getListenerPort(), AppConfig.chordState.getNextNodePortTest(),
                        "newMessageText",clientMessage.getSusPort(),clientMessage.getOriginalSenderPort());
                MessageUtil.sendMessage(impostorMessage);

                AppConfig.deadNodes.add(AppConfig.chordState.getSuccessorTable()[0].getListenerPort());

            } else {

                AppConfig.timestampedErrorPrint("BRISEMO FINAL "+clientMessage.getSusPort());

               AppConfig.chordState.removeNodes(clientMessage.getSusPort());
            }

        }


    }


}
