package ml.liuchibing.ControlledUDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
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

    private void Filt() {
        byte[] buffer = new byte[1024 * 1024 * 2];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        datagramSocket.receive(packet);
    }

    /**
     * Accept connecting and return a CUDPSessionSocket for the connection.
     *
     * @return a CUDPSessionSocket for the connection.
     */
    public CUDPSessionSocket Accept() {

    }
}
