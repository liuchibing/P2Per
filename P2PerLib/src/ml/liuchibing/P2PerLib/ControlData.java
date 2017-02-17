package ml.liuchibing.P2PerLib;

/**
 * 解析过的命令.
 * 用于初步解析Control数据包.
 * Created by charlie on 2017/2/17.
 */
public class ControlData {
    String command;
    String content;

    public ControlData(String origin) {
        //截取Command
        command = origin.substring(SharedCodes.CURRENT_VERSION_HEAD.length() + 1, (origin.matches(SharedCodes.REGEX_PACKET_HAS_CONTENT)) ? origin.indexOf("{") : origin.length() - 1);
        //截取JSON格式的Content
        content = "";
        if (origin.matches(SharedCodes.REGEX_PACKET_HAS_CONTENT)) {
            content = origin.substring(origin.indexOf("{"), origin.lastIndexOf("}"));
        }
    }
}
