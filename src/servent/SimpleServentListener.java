package servent;

import app.AppConfig;
import app.Cancellable;
import servent.handler.*;
import servent.message.CheckingMessage;
import servent.message.ImpostorMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.util.MessageUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleServentListener implements Runnable, Cancellable {

    private volatile boolean working = true;
    private volatile boolean flag = true;
    private volatile int deadNode = 0;

    public SimpleServentListener() {

    }

    /*
     * Thread pool for executing the handlers. Each client will get it's own handler thread.
     */
    private final ExecutorService threadPool = Executors.newWorkStealingPool();

    @Override
    public void run() {
        ServerSocket listenerSocket = null;
        try {
            listenerSocket = new ServerSocket(AppConfig.myServentInfo.getListenerPort(), 100);
            /*
             * If there is no connection after 1s, wake up and see if we should terminate.
             */
            listenerSocket.setSoTimeout(1000);
        } catch (IOException e) {
            AppConfig.timestampedErrorPrint("Couldn't open listener socket on: " + AppConfig.myServentInfo.getListenerPort());
            System.exit(0);
        }


        while (working) {

            if (System.currentTimeMillis() - AppConfig.countDown.get() > 7000 && AppConfig.sendPing.get() && AppConfig.countDown.get() != 0 && !AppConfig.deadNodes.contains(AppConfig.chordState.getSuccessorTable()[0].getListenerPort())) {
                //TODO IZBRISI NODE

                //    AppConfig.timestampedErrorPrint("I am id " + AppConfig.myServentInfo.getChordId() + " and i'm very dead for " + AppConfig.chordState.getSuccessorTable()[0].getChordId());

                Message impostorMessage = new ImpostorMessage(AppConfig.myServentInfo.getListenerPort(),
                        AppConfig.chordState.getNextSuccesorNodePort(AppConfig.chordState.getSuccessorTable()[0].getListenerPort()),
                        "mrs",
                        AppConfig.chordState.getSuccessorTable()[0].getListenerPort(),
                        AppConfig.myServentInfo.getListenerPort()
                );
                MessageUtil.sendMessage(impostorMessage);
                AppConfig.timestampedErrorPrint("SALJEM IMPOSTOR MESSAGE da ne radi " + AppConfig.chordState.getSuccessorTable()[0].getListenerPort());
                AppConfig.deadNodes.add(AppConfig.chordState.getSuccessorTable()[0].getListenerPort());
                AppConfig.countDown.set(0);
                AppConfig.sendPing.set(false);
            } else if (System.currentTimeMillis() - AppConfig.countDown.get() > 4000 && AppConfig.sendPing.get() && AppConfig.countDown.get() != 0 /*&& flag*/) {
                flag = false;
                //  AppConfig.timestampedErrorPrint("I am id " + AppConfig.myServentInfo.getChordId() + " and veoma sumnjivo for " + AppConfig.chordState.getSuccessorTable()[0].getChordId());

                int nestSucc = 0;
                for (int i = 0; i < AppConfig.chordState.getSuccessorTable().length; i++) {
                    if (AppConfig.chordState.getSuccessorTable()[i].getListenerPort() != AppConfig.chordState.getSuccessorTable()[0].getListenerPort() && AppConfig.chordState.getSuccessorTable()[i].getListenerPort() != 0) {
                        nestSucc = AppConfig.chordState.getSuccessorTable()[i].getListenerPort();
                        break;
                    }
                }
                if (AppConfig.chordState.getSuccessorTable()[0] != null && AppConfig.chordState.getPredecessor() != null && nestSucc != 0 /*&& !AppConfig.sendPing.get()*/) {

                    Message pingMsgSuc = new CheckingMessage(MessageType.CHECKING, AppConfig.myServentInfo.getListenerPort(),
                            nestSucc, System.currentTimeMillis(), AppConfig.chordState.getSuccessorTable()[0].getListenerPort(),false);
                    MessageUtil.sendMessage(pingMsgSuc);
                    //     AppConfig.timestampedErrorPrint("PongHandler: Sending checking message to next successor " + nestSucc);
                }

            }


            try {


                Message clientMessage;

                Socket clientSocket = listenerSocket.accept();

                //GOT A MESSAGE! <3
                clientMessage = MessageUtil.readMessage(clientSocket);

                MessageHandler messageHandler = new NullHandler(clientMessage);

                /*
                 * Each message type has it's own handler.
                 * If we can get away with stateless handlers, we will,
                 * because that way is much simpler and less error prone.
                 */
                //    AppConfig.timestampedErrorPrint("I am id "+AppConfig.myServentInfo.getChordId());


                switch (clientMessage.getMessageType()) {
                    case NEW_NODE:
                        messageHandler = new NewNodeHandler(clientMessage);
                        break;
                    case WELCOME:
                        messageHandler = new WelcomeHandler(clientMessage);
                        break;
                    case SORRY:
                        messageHandler = new SorryHandler(clientMessage);
                        break;
                    case UPDATE:
                        messageHandler = new UpdateHandler(clientMessage);
                        break;
                    case PUT:
                        messageHandler = new PutHandler(clientMessage);
                        break;
                    case ASK_GET:
                        messageHandler = new AskGetHandler(clientMessage);
                        break;
                    case TELL_GET:
                        messageHandler = new TellGetHandler(clientMessage);
                        break;
                    case POISON:
                        break;
                    case PING:
                        messageHandler = new PingHandler(clientMessage);
                        break;
                    case PONG:
                        //      AppConfig.recievedPong.set(true);
                        messageHandler = new PongHandler(clientMessage);
                        break;
                    case CHECKING:
                        messageHandler = new CheckingHandler(clientMessage);
                        break;
                    case VIEW:
                        messageHandler = new ViewHandler(clientMessage);
                        break;
                    case IMPOSTOR:
                        messageHandler = new ImpostorHandler(clientMessage);
                        break;
                    case CREATE_COPY:
                        messageHandler = new CreateCopyHandler(clientMessage);
                        break;
                    case TOKEN:
                        messageHandler = new TokenHandler(clientMessage);
                        break;
                }

                threadPool.submit(messageHandler);
            } catch (SocketTimeoutException timeoutEx) {
                //Uncomment the next line to see that we are waking up every second.
//				AppConfig.timedStandardPrint("Waiting...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop() {
        AppConfig.timestampedStandardPrint("Listener stopping...");
        this.working = false;
    }

}
