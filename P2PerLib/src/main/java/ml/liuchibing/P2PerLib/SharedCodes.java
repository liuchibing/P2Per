package ml.liuchibing.P2PerLib;

import javax.xml.crypto.Data;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * 存储了一系列P2Per需要用到的常量
 * Created by charlie on 2017/2/15.
 */
public class SharedCodes {
    public static final String CURRENT_VERSION_HEAD = "P2PerTP/1.0";

    public static final String COMMAND_OK = "OK";
    public static final String COMMAND_ERROR = "ERROR";
    public static final String COMMAND_REFUSE = "REFUSE";

    public static final String COMMAND_LOGIN = "LOGIN";
    public static final String COMMAND_LOGOUT = "LOGOUT";
    public static final String COMMAND_GET_PEER_BY_NAME = "GET_PEER_BY_NAME";
    public static final String COMMAND_GET_PEER_BY_UUID = "GET_PEER_BY_UUID";

    public static final String REGEX_PACKET_HAS_CONTENT = "\\{.+\\}";

    /**
     * 发送Control数据.
     * @param command
     * @param content
     * @param destination
     * @param socket
     * @throws IOException
     */
    public static void SendControlData(String command, String content, InetSocketAddress destination, DatagramSocket socket) throws IOException {
        //构建Control字符串
        StringBuilder sb = new StringBuilder(SharedCodes.CURRENT_VERSION_HEAD);
        sb.append(" ");
        sb.append(command);
        if (content != null) {
            sb.append(" ");
            sb.append(content);
        }

        //构建buffer
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(buffer);
        dos.writeByte((byte)PacketType.Control.ordinal());//写入包类型
        byte[] data = sb.toString().getBytes("utf-8");//编码数据
        dos.write(data, 0, data.length);//写入数据
        socket.send(new DatagramPacket(buffer.toByteArray(), buffer.toByteArray().length, destination));

        //关闭流
        buffer.close();
        dos.close();
    }

}
