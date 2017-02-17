package ml.liuchibing.P2PerLib;

import com.sun.istack.internal.Nullable;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.time.LocalTime;
import java.util.List;

import com.google.gson.*;

/**
 * P2P中介转发服务器.
 *
 * @author liuchibing
 *         Created by charlie on 2017/2/13.
 */
public class Server {
    private int localPort;
    private DatagramSocket socket;
    private List<PeerInfo> peers;
    private Logger _logger;

    /**
     * 初始化一个P2Per中介转发服务器.
     *
     * @param port   要绑定监听的端口
     * @param logger (可选)负责处理日志记录的logger
     */
    public Server(int port, @Nullable Logger logger) {
        if (logger != null) _logger = logger;
        localPort = port;

        try {
            socket = new DatagramSocket(localPort);
        } catch (SocketException e) {
            Log(e.toString());
        }
    }

    /**
     * 在新线程里启动服务器.
     */
    public void StartAsync() {
        new Thread(this::Start).start();
    }

    /**
     * 启动服务器进入消息循环.
     * 会阻塞线程.
     */
    public void Start() {
        try {
            byte[] buffer = new byte[1024 * 1024 * 2];//2M buffer
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            Log("Server: 进入消息循环");
            while (true) {
                socket.receive(packet);
                //在新线程里与Client交互，主线程继续监听
                new Thread(() -> {
                    Response(packet);
                }).start();
            }
        } catch (IOException e) {
            Log(e.toString());
        }
    }

    /**
     * 处理客户端请求.
     *
     * @param packet 收到的数据包
     */
    private void Response(DatagramPacket packet) {
        byte[] buffer = packet.getData();
        int length = packet.getLength();
        if (buffer[0] == PacketType.Control.ordinal()) {
            try {
                String request = new String(buffer, 1, length - 1, "utf-8");
                //拒绝不支持的版本号
                if (!request.startsWith(SharedCodes.CURRENT_VERSION_HEAD)) {
                    Log("Server: 客户端版本号不受支持.");
                    SendControlData(SharedCodes.COMMAND_REFUSE, null, packet.getSocketAddress());
                    return;
                }
                ControlData data = new ControlData(request);

            } catch (UnsupportedEncodingException e) {
                Log("Server: 收到未知数据包。");
            }
        }
    }

    private boolean SendControlData(String command, String content, SocketAddress destination) {
        boolean result = false;

        StringBuilder sb = new StringBuilder(SharedCodes.CURRENT_VERSION_HEAD);
        sb.append(" ");
        sb.append(command);
        if (content != null) {
            sb.append(" ");
            sb.append(content);
        }

        try {
            byte[] origin = sb.toString().getBytes("utf-8");
            byte[] data = new byte[origin.length + 1];
            data[0] = (byte) PacketType.Control.ordinal();
            for (int i = 0; i < origin.length; i++) {
                data[i + 1] = origin[i];
            }
            socket.send(new DatagramPacket(data, data.length, destination));
        } catch (IOException e) {
            Log(e.toString());
        }
        return result;
    }

    private void Log(String msg) {
        if (_logger != null) _logger.Log(msg);
    }
}
