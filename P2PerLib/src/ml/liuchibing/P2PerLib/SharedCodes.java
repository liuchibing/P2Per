package ml.liuchibing.P2PerLib;

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
    public static final String COMMAND_GET_PEER_BY_NAME = "GET_PEER_BY_NAME";
    public static final String COMMAND_GET_PEER_BY_UUID = "GET_PEER_BY_UUID";

    public static final String REGEX_PACKET_HAS_CONTENT = "\\{.+\\}";

}
