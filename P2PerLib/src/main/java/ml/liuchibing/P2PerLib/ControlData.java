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
        String headless = origin.replace(SharedCodes.CURRENT_VERSION_HEAD, "").trim();
        command = headless.split("\\s")[0].trim();
        //截取Content
        content = headless.replace(command, "").trim();
    }
}
