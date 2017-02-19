package ml.liuchibing.P2PerLib;

import com.sun.istack.internal.Nullable;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * P2P客户端.
 *
 * @author liuchibing
 *         Created by charlie on 2017/2/19.
 */
public class Client {
    private DatagramSocket socket;
    private int localPort;
    private Logger _logger;
    private String _serverIp;
    private String _username;

    public Client(String username, String serverIp, int port, @Nullable Logger logger) {
        if (logger != null) _logger = logger;
        localPort = port;
        _serverIp = serverIp;
        _username = username;

        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            Log(e.toString());
        }
    }

    public void Start() {
        try {
            byte[] buffer = (SharedCodes.COMMAND_LOGIN + " " + _username).getBytes("utf-8");
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            packet.setSocketAddress(new InetSocketAddress(_serverIp, 42800));
            Log(new InetSocketAddress(_serverIp, 42800).toString());
            socket.send(packet);
            while (true) {
                byte[] buffer2 = new byte[1024 * 1024 * 2];
                DatagramPacket packet2 = new DatagramPacket(buffer2, buffer2.length);
                socket.receive(packet2);
                Log(new String(packet2.getData(), "utf-8"));
            }
        } catch (IOException e) {
            Log(e.toString());
        }
    }

    private void Log(String msg) {
        if (_logger != null) _logger.Log(msg);
    }
}
