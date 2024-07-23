package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.ViewMessage;
import servent.message.util.MessageUtil;

import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;

public class ViewHandler implements MessageHandler {


    private Message clientMessage;
    private volatile boolean flag = false;

    public ViewHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {

        if (clientMessage.getMessageType() == MessageType.VIEW) {
            if (clientMessage.getViewPort() != AppConfig.myServentInfo.getListenerPort() && clientMessage.getOriginalSenderPort() != AppConfig.myServentInfo.getListenerPort()) {

                CopyOnWriteArrayList<File> files = clientMessage.getNodesListFiles();

                if (files.isEmpty()) {
                    AppConfig.timestampedErrorPrint("ViewHandler");
                    boolean provera = false;
                    if (!AppConfig.myServentInfo.friendList.isEmpty()) {
                        for (int i = 0; i < AppConfig.myServentInfo.friendList.size(); i++) {
                            int port = Integer.parseInt(AppConfig.myServentInfo.friendList.get(i).split(":")[1]);
                            if (port == clientMessage.getOriginalSenderPort()) {
                                provera = true;
                                break;
                            }
                        }
                    }
                    if (provera) {
                        // Get the list from the private copy map
                        CopyOnWriteArrayList<File> privateFiles = AppConfig.chordState.getValueMapFilePrivateCopy().get(clientMessage.getViewPort());
                        // Only assign if privateFiles is not null
                        if (privateFiles != null) {
                            files = privateFiles;
                            if (AppConfig.chordState.getValueMapFilePublicCopy().get(clientMessage.getViewPort()) != null) {
                                files.addAll(AppConfig.chordState.getValueMapFilePublicCopy().get(clientMessage.getViewPort()));
                            }
                        } else {
                            files = new CopyOnWriteArrayList<>(); // Or handle it according to your needs, like setting it to null or throwing an exception
                        }
                    } else {
                        // Get the list from the public copy map
                        CopyOnWriteArrayList<File> publicFiles = AppConfig.chordState.getValueMapFilePublicCopy().get(clientMessage.getViewPort());
                        // Only assign if publicFiles is not null
                        if (publicFiles != null) {
                            files = publicFiles;
                        } else {
                            files = new CopyOnWriteArrayList<>(); // Or handle it according to your needs, like setting it to null or throwing an exception
                        }
                    }
                    AppConfig.timestampedErrorPrint("Sending COPY OF files " + AppConfig.chordState.getValueMapFilePrivateCopy() + " " + AppConfig.chordState.getValueMapFilePublicCopy()
                            + " " + files + " " + clientMessage.getViewPort());
                }
                AppConfig.timestampedErrorPrint("Sending COPY OF files ss " + files);

                Message viewMessage = new ViewMessage(MessageType.VIEW, AppConfig.myServentInfo.getListenerPort(),
                        AppConfig.chordState.getSuccessorTable()[0].getListenerPort(), files, clientMessage.getViewPort(), clientMessage.getOriginalSenderPort());
                MessageUtil.sendMessage(viewMessage);
                AppConfig.timestampedErrorPrint("Sending to next!");
            } else {
                AppConfig.timestampedErrorPrint("PROVERAVAM " + clientMessage.getNodesListFiles() + " I " + clientMessage.getNodesListFiles().isEmpty());
                if (clientMessage.getNodesListFiles().isEmpty()) {
                    if (!AppConfig.myServentInfo.friendList.isEmpty()) {
                        for (int i = 0; i < AppConfig.myServentInfo.friendList.size(); i++) {

                            int port = Integer.parseInt(AppConfig.myServentInfo.friendList.get(i).split(":")[1]);
                            if (port == clientMessage.getOriginalSenderPort()) {
                                flag = true;
                                break;
                            }
                        }
                    }
                    if (flag) {
                        CopyOnWriteArrayList<File> files = new CopyOnWriteArrayList<>();
                        files.addAll(AppConfig.chordState.getValueMapFilePublic().values());
                        files.addAll(AppConfig.chordState.getValueMapFilePrivate().values());
                        Message viewMessage = new ViewMessage(MessageType.VIEW, AppConfig.myServentInfo.getListenerPort(),
                                AppConfig.chordState.getSuccessorTable()[0].getListenerPort(), files, clientMessage.getViewPort(), clientMessage.getOriginalSenderPort());
                        MessageUtil.sendMessage(viewMessage);
                        flag = false;
                        AppConfig.timestampedErrorPrint("Sending private files " + files);
                    } else {
                        AppConfig.timestampedErrorPrint("Return files");
                        AppConfig.timestampedErrorPrint("Sending public files " + " " + AppConfig.chordState.getValueMapFilePrivate().values());
                        CopyOnWriteArrayList<File> files1 = new CopyOnWriteArrayList<>(AppConfig.chordState.getValueMapFilePublic().values());
                        Message viewMessage = new ViewMessage(MessageType.VIEW, AppConfig.myServentInfo.getListenerPort(),
                                AppConfig.chordState.getSuccessorTable()[0].getListenerPort(), files1, clientMessage.getViewPort(), clientMessage.getOriginalSenderPort());
                        MessageUtil.sendMessage(viewMessage);
                    }
                } else {
                    AppConfig.timestampedErrorPrint("Show files");
                    for (File file : clientMessage.getNodesListFiles()) {
                        AppConfig.timestampedErrorPrint(file.getName());
                    }

                }
            }


        }

    }
}
