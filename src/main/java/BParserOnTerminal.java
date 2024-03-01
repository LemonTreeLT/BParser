import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class BParserOnTerminal {
    private static final Logger logger = new Logger(Logger.LoggerLevel.ALL, "BParser");
    private static String clipboardPast = null;
    private static final Utils utils = new Utils(logger);

    public static void run() {
        utils.introduce();

        Timer timer = new Timer();

        TimerTask regularTask = new TimerTask() {
            @Override
            public void run() {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                try {
                    // 获取剪贴板内容
                    Transferable contents = clipboard.getContents(null);
                    // 如果剪贴板内容不为空，且支持字符串数据类型，且与上一次剪贴板内容不同
                    if(contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor) &&
                            !Objects.equals(clipboardPast, contents.getTransferData(DataFlavor.stringFlavor))) {

                        // 获取剪贴板中的文本
                        String copiedText = (String) contents.getTransferData(DataFlavor.stringFlavor);
                        // 解析文本
                        String parserText = utils.getVideoInfo(copiedText);

                        // 如果解析结果不为空，则将解析结果设置到剪贴板
                        if(parserText != null) clipboard.setContents(new StringSelection(parserText), null);
                        // 再次获取剪贴板内容
                        contents = clipboard.getContents(null);
                        // 更新上一次剪贴板内容
                        clipboardPast = (String) contents.getTransferData(DataFlavor.stringFlavor);

                    }
                } catch(IOException | UnsupportedFlavorException e) {
                    logger.Error("发生未知错误: " + e.getMessage());
                }
            }
        };
        timer.schedule(regularTask, 0, 1000);

    }
}
