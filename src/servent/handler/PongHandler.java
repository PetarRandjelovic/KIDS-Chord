package servent.handler;

import app.AppConfig;
import servent.message.CheckingMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.util.MessageUtil;

public class PongHandler implements MessageHandler {

    private Message clientMessage;

    public PongHandler(Message clientMessage) {
        this.clientMessage = clientMessage;

    }

    @Override
    public void run() {

        if (clientMessage.getMessageType() == MessageType.PONG) {

            if(AppConfig.myServentInfo.getListenerPort()== 1300){
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }


         //   if (clientMessage.getSenderPort() == AppConfig.chordState.getSuccessorTable()[0].getListenerPort()) {
      //    Long s=  AppConfig.pingTimestampsMap.get(clientMessage.getSenderPort());
     //       if(s!=null) {
         //   AppConfig.timestampedErrorPrint("Pong received "+ AppConfig.pingTimestampsMap.get(clientMessage.getSenderPort()));

            AppConfig.recievedPong.set(true);

                Long pingTime = clientMessage.getPingTime();
                Long d=System.currentTimeMillis() - pingTime;
         //   AppConfig.timestampedErrorPrint("Pong received "+d);
                //      AppConfig.timestampedErrorPrint("PongHandler: Pong from successor received" + pingTime + " " + System.currentTimeMillis());

            AppConfig.sendPing.set(false);
            AppConfig.countDown.set(0);

            if(pingTime!=0) {
                if (System.currentTimeMillis() - pingTime > 10000) {
//                    AppConfig.timestampedErrorPrint("PongHandler: Pong from successor is super late " + clientMessage.getSenderPort());
                } else if (System.currentTimeMillis() - pingTime > 4000) {
//                    AppConfig.timestampedErrorPrint("PongHandler: Pong from successor is late " + clientMessage.getSenderPort());
//
//                    int nestSucc = 0;
//                    for (int i = 0; i < AppConfig.chordState.getSuccessorTable().length; i++) {
//                        if (AppConfig.chordState.getSuccessorTable()[i].getListenerPort() != clientMessage.getSenderPort() && AppConfig.chordState.getSuccessorTable()[i].getListenerPort() != 0) {
//                            nestSucc = AppConfig.chordState.getSuccessorTable()[i].getListenerPort();
//                            break;
//                        }
//                    }
//                    if (AppConfig.chordState.getSuccessorTable()[0] != null && AppConfig.chordState.getPredecessor() != null && nestSucc != 0 /*&& !AppConfig.sendPing.get()*/) {
//
//                        Message pingMsgSuc = new CheckingMessage(MessageType.CHECKING, AppConfig.myServentInfo.getListenerPort(),
//                                nestSucc, System.currentTimeMillis(),clientMessage.getSenderPort());
//                        MessageUtil.sendMessage(pingMsgSuc);
//                        AppConfig.timestampedErrorPrint("PongHandler: Sending checking message to next successor " + nestSucc);
//                    }

                } else {
               //     AppConfig.timestampedErrorPrint("PongHandler: Pong from successor is on time");
                }
            }
      //      }
       //     }
//            } else if (clientMessage.getSenderPort() == AppConfig.chordState.getPredecessor().getListenerPort()) {
//                Long pingTime = AppConfig.pingTimestampsMap.get(AppConfig.chordState.getPredecessor().getListenerPort());
//
//                //     AppConfig.timestampedErrorPrint("PongHandler: Pong from predecessor received" + pingTime + " " + System.currentTimeMillis());
//
//            }

//           if(pingTime+4000>System.currentTimeMillis()){
//               AppConfig.timestampedErrorPrint("PongHandler: Pong from successor is late");
//           }else {
//              AppConfig.timestampedErrorPrint("PongHandler: Pong from successor is on time");
//           }
        }

    }
}
