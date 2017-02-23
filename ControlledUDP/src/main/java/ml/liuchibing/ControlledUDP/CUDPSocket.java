package ml.liuchibing.ControlledUDP;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Hashtable;

/**
 * Controlled-UDP Socket.
 *
 * @author liuchibing
 */
public class CUDPSocket {
    private DatagramSocket datagramSocket;
    private int localPort;
    private Hashtable<Integer, CUDPSessionSocket> sessions;

    public CUDPSocket(InetAddress address, int port) throws SocketException {
        datagramSocket = new DatagramSocket(port, address);
        localPort = port;
        sessions = new Hashtable<>();
    }

    /**
     * Receive data packets and distribute the packets to the right handler.
     */
    private void Filter() {
        while (true) {
            byte[] buffer = new byte[1024 * 1024 * 2];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                datagramSocket.receive(packet);
                //Begin to read header.
                ByteArrayInputStream data = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                DataInputStream stream = new DataInputStream(data);
                //Initialize a Packet
                ControlledUDPPacket cPacket = new ControlledUDPPacket();
                //Get Session Id
                cPacket.setSessionId(stream.readInt());
                //Get Sequence Number
                cPacket.setSequenceNumber(stream.readInt());
                //Get Control Message
                cPacket.setControlMessage(stream.readByte());
                //Get Data
                cPacket.setData(Arrays.copyOfRange(packet.getData(), 9, packet.getLength() - 1));

                //Filter packet
                if(sessions.containsKey(cPacket.getSessionId())) {
                    sessions.get(cPacket.getSessionId())
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Accept connecting and return a CUDPSessionSocket for the connection.
     *
     * @return a CUDPSessionSocket for the connection.
     */
    public CUDPSessionSocket Accept() {

    }
}
