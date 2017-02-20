package ml.liuchibing.P2PerLib;

import com.sun.istack.internal.Nullable;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.UUID;

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

    public Client(String username, UUID uuid, String serverIp, int port, @Nullable Logger logger) {
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

    private void Log(String msg) {
        if (_logger != null) _logger.Log(msg);
    }
}
