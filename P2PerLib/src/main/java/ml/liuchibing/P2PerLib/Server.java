package ml.liuchibing.P2PerLib;

import com.sun.istack.internal.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;
import java.util.UUID;

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
    private ArrayList<PeerInfo> peers;
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
            InetSocketAddress address = new InetSocketAddress(InetAddress.getLocalHost(), localPort);
            socket = new DatagramSocket(address);
            Log(address.toString());
        } catch (SocketException | UnknownHostException e) {
            Log(e.toString());
        }

        peers = new ArrayList<>();
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
            Log("Server: 进入消息循环");
            while (true) {
                byte[] buffer = new byte[1024 * 1024 * 2];//2M buffer
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                //在新线程里与Client交互，主线程继续监听
                new Thread(() -> Response(packet)).start();
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
                    try {
                        SharedCodes.SendControlData(SharedCodes.COMMAND_REFUSE, null, (InetSocketAddress) packet.getSocketAddress(), socket);
                    } catch (IOException e) {
                        Log(e.toString());
                    }
                    return;
                }
                //初步分析数据
                ControlData data = new ControlData(request);
                //按Command内容进行响应
                switch (data.command) {
                    case SharedCodes.COMMAND_LOGIN: //客户端登录
                        PeerInfo peer = new PeerInfo();
                        peer.username = data.content.split("\\s")[0];
                        peer.ip = packet.getAddress().toString();
                        peer.port = packet.getPort();
                        peer.uuid = UUID.fromString(data.content.split("\\s")[1]);
                        //防止重复
                        boolean hasLogged = false;
                        for (PeerInfo item :
                                peers) {
                            if (item.uuid == peer.uuid) {
                                hasLogged = true;
                            }
                        }
                        if (!hasLogged) peers.add(peer);
                        try {
                            SharedCodes.SendControlData(
                                    SharedCodes.COMMAND_OK, "", (InetSocketAddress) packet.getSocketAddress(),
                                    socket);
                        } catch (IOException e) {
                            Log(e.toString());
                        }
                        Log("Server: 收到客户端登录：" + packet.getAddress().toString() + ":" + packet.getPort());
                        break;
                    case SharedCodes.COMMAND_LOGOUT://客户端下线
                        for (PeerInfo item :
                                peers) {
                            if (item.uuid.toString().equals(data.content)) peers.remove(item);
                        }
                        break;
                    case SharedCodes.COMMAND_GET_PEER_BY_NAME://客户端通过用户名查询客户端
                        Gson gson = new Gson();
                        String name = data.content;
                        ArrayList<String> results = new ArrayList<>();
                        for (PeerInfo item :
                                peers) {
                            if (item.username.equals(name)) {
                                results.add(gson.toJson(item, PeerInfo.class));
                            }
                        }
                        if (results.size() != 0) {
                            String[] resultsArray = new String[results.size()];
                            try {
                                SharedCodes.SendControlData(SharedCodes.COMMAND_OK, gson.toJson(results.toArray(resultsArray), String[].class), (InetSocketAddress) packet.getSocketAddress(), socket);
                            } catch (IOException e) {
                                Log(e.toString());
                            }
                        }
                        break;
                    case SharedCodes.COMMAND_GET_PEER_BY_UUID://客户端通过UUID查询客户端
                        Gson gsonU = new Gson();
                        String uuid = data.content;
                        ArrayList<String> resultsU = new ArrayList<>();
                        for (PeerInfo item :
                                peers) {
                            if (item.uuid.toString().equals(uuid)) {
                                resultsU.add(gsonU.toJson(item, PeerInfo.class));
                            }
                        }
                        if (resultsU.size() != 0) {
                            String[] resultsArray = new String[resultsU.size()];
                            try {
                                SharedCodes.SendControlData(SharedCodes.COMMAND_OK, gsonU.toJson(resultsU.toArray(resultsArray), String[].class), (InetSocketAddress) packet.getSocketAddress(), socket);
                            } catch (IOException e) {
                                Log(e.toString());
                            }
                        }
                        break;
                }
            } catch (UnsupportedEncodingException e) {
                Log("Server: 收到未知数据包。");
            }
        }
    }

    private void Log(String msg) {
        if (_logger != null) _logger.Log(msg);
    }
}
