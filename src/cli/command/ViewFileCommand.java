package cli.command;

import app.AppConfig;
import mutex.SuzukiKasami;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.PingMessage;
import servent.message.ViewMessage;
import servent.message.util.MessageUtil;

import java.util.concurrent.CopyOnWriteArrayList;

public class ViewFileCommand implements CLICommand {
    @Override
    public String commandName() {
        return "view_files";
    }

    @Override
    public void execute(String args) {
        String[] splitArgs = args.split(":");

        if (splitArgs.length == 2) {

            if (splitArgs[0].equals("localhost") && isInteger(splitArgs[1])) {
                int port = Integer.parseInt(splitArgs[1]);
                while ( AppConfig.chordState.getSuccessorTable()[0] == null) {
                 //   AppConfig.timestampedErrorPrint("Predecessor is null. Waiting...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

         //       SuzukiKasami.lock();
              AppConfig.timestampedErrorPrint("Send view files to check for "+port);
                Message viewMessage = new ViewMessage(MessageType.VIEW, AppConfig.myServentInfo.getListenerPort(),
                        AppConfig.chordState.getSuccessorTable()[0].getListenerPort(), new CopyOnWriteArrayList<>(),port, AppConfig.myServentInfo.getListenerPort());
                MessageUtil.sendMessage(viewMessage);

         //       SuzukiKasami.unlock();
            }

        }
    }

    public boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
