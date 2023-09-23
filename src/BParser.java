import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class BParser {
    public static final Logger logger = new Logger(Logger.LoggerLevel.ALL, "BParser");
    private static int errorCount = 0;
    private static String clipboardPast = null;

    public static void main(String[] args) {
        Timer timer = new Timer();

        TimerTask regularTask = new TimerTask() {
            @Override
            public void run() {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

                try {
                    Transferable contents = clipboard.getContents(null);
                    if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor) &&
                            !Objects.equals(clipboardPast, contents.getTransferData(DataFlavor.stringFlavor))) {

                        String copiedText = (String) contents.getTransferData(DataFlavor.stringFlavor);
                        String parserText = Utils.getVideoInfo(copiedText);

                        if(parserText != null) clipboard.setContents(new StringSelection(parserText), null);
                        contents = clipboard.getContents(null);
                        clipboardPast = (String) contents.getTransferData(DataFlavor.stringFlavor);

                    }
                } catch (UnsupportedFlavorException | IOException e) {
                    errorCount ++;
                    logger.Error("发生未知错误: " + e.getMessage());
                    if (Constant.ALLOW_ERROR - errorCount > 0) {
                        logger.Warn("再发生 " + (Constant.ALLOW_ERROR - errorCount) + " 次程序将强行停止");
                    } else {
                        logger.Info("报错过多,程序停止");
                    }
                }
            }
        };
        timer.schedule(regularTask, 0, 1000);

    }
}
