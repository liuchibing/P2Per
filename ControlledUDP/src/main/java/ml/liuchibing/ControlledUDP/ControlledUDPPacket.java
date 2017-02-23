package ml.liuchibing.ControlledUDP;

/**
 * A data packet of Controlled-UDP.
 * Packet Structure:<code>
 * +-----------------+----------------------+-----------------------+--------------+
 * | Session-ID: Int | Sequence Number: Int | Control-Message: Byte | Data: Byte[] |
 * |     4 bytes     |        4 bytes       |         1 byte        |              |
 * +-----------------+----------------------+-----------------------+--------------+
 * </code>
 *
 * @author liuchibing
 */
public class ControlledUDPPacket {
    private int sessionId;
    private int sequenceNumber;
    private byte controlMessage;
    private byte[] data;

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public byte getControlMessage() {
        return controlMessage;
    }

    public void setControlMessage(byte controlMessage) {
        this.controlMessage = controlMessage;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
