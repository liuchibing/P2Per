package ml.liuchibing.ControlledUDP;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Socket of a session.
 * Created by charlie on 2017/2/22.
 */
public class CUDPSessionSocket {
    private int sessionId;
    private ArrayBlockingQueue<ControlledUDPPacket> queue = new ArrayBlockingQueue<ControlledUDPPacket>(1);

    public CUDPSessionSocket() {

    }


    public ArrayBlockingQueue<ControlledUDPPacket> getQueue() {
        return queue;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
}
