package servent.handler;

import app.AppConfig;
import servent.message.CreateCopyMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.util.MessageUtil;

import java.util.concurrent.CopyOnWriteArrayList;

public class CreateCopyHandler implements MessageHandler{

    private Message clientMessage;
    public CreateCopyHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }


    @Override
    public void run() {


       if (clientMessage.getMessageType()== MessageType.CREATE_COPY){
            String messageText[] = clientMessage.getMessageText().split(":");
           if(clientMessage.getMessageText().contains("first")){
           //         AppConfig.timestampedErrorPrint("PRVI PUT KREIRAM KOPIJU");

               if(clientMessage.getMessageText().contains("public")) {
                   AppConfig.chordState.getValueMapFilePublicCopy().computeIfAbsent(clientMessage.getOriginalSenderPort(), k -> new CopyOnWriteArrayList<>()).add(clientMessage.getFile());
              //     AppConfig.timestampedErrorPrint("POSTOJI KOPIJA "+AppConfig.chordState.getValueMapFilePublicCopy().get(clientMessage.getOriginalSenderPort()));
               }else {
                   AppConfig.chordState.getValueMapFilePrivateCopy().computeIfAbsent(clientMessage.getOriginalSenderPort(), k -> new CopyOnWriteArrayList<>()).add(clientMessage.getFile());
             //     AppConfig.timestampedErrorPrint("POSTOJI KOPIJA "+AppConfig.chordState.getValueMapFilePrivateCopy().get(clientMessage.getOriginalSenderPort()));
               }

               Message createCopyMessage = new CreateCopyMessage(AppConfig.myServentInfo.getListenerPort(), AppConfig.chordState.getNextNodePort(), clientMessage.getFile(), clientMessage.getOriginalSenderPort(), "public:second");
               MessageUtil.sendMessage(createCopyMessage);

           } else {
          //     AppConfig.timestampedErrorPrint("DRUGI PUT KREIRAM KOPIJU");

               if(clientMessage.getMessageText().contains("public")) {

                   AppConfig.chordState.getValueMapFilePublicCopy().computeIfAbsent(clientMessage.getOriginalSenderPort(), k -> new CopyOnWriteArrayList<>()).add(clientMessage.getFile());
             //      AppConfig.timestampedErrorPrint("POSTOJI KOPIJA "+AppConfig.chordState.getValueMapFilePublicCopy().get(clientMessage.getOriginalSenderPort()));
               }else {
                   AppConfig.chordState.getValueMapFilePrivateCopy().computeIfAbsent(clientMessage.getOriginalSenderPort(), k -> new CopyOnWriteArrayList<>()).add(clientMessage.getFile());
            //       AppConfig.timestampedErrorPrint("POSTOJI KOPIJA "+AppConfig.chordState.getValueMapFilePrivateCopy().get(clientMessage.getOriginalSenderPort()));
               }

           }
        }
    }
}
