package app;

import servent.message.Message;
import servent.message.MessageType;
import servent.message.PingMessage;
import servent.message.util.MessageUtil;

public class PingPongThread implements Runnable, Cancellable {

    //  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private volatile boolean working = true;

    public PingPongThread() {
    }

    @Override
    public void run() {
        while (working) {


            if (AppConfig.chordState.getSuccessorTable()[0] != null && AppConfig.chordState.getPredecessor() != null && !AppConfig.sendPing.get()) {

                Long time = System.currentTimeMillis();

                if (AppConfig.recievedPong.get()) {
                    AppConfig.countDown.set(time);
                    AppConfig.recievedPong.set(false);
                }

                AppConfig.sendPing.set(true);

                AppConfig.pingTimestampsMap.put(AppConfig.chordState.getSuccessorTable()[0].getListenerPort(), time);

                Message pingMsgSuc = new PingMessage(MessageType.PING, AppConfig.myServentInfo.getListenerPort(),
                        AppConfig.chordState.getSuccessorTable()[0].getListenerPort(), System.currentTimeMillis());
                MessageUtil.sendMessage(pingMsgSuc);

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }


        }
    }

    public void stop() {
        AppConfig.timestampedErrorPrint("PingPongThread stoped");
        this.working = false;
    }


}
