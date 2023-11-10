import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class BParserOnTerminal {
    public static final Logger logger = new Logger(Logger.LoggerLevel.ALL, "BParser");
    private static String clipboardPast = null;
    private static final Utils utils = new Utils(logger);

    public static void main(String[] args) {
        utils.introduce();

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
                        String parserText = utils.getVideoInfo(copiedText);

                        if(parserText != null) clipboard.setContents(new StringSelection(parserText), null);
                        contents = clipboard.getContents(null);
                        clipboardPast = (String) contents.getTransferData(DataFlavor.stringFlavor);

                    }
                } catch (IOException | UnsupportedFlavorException e) {
                    logger.Error("发生未知错误: " + e.getMessage());
                }
            }
        };
        timer.schedule(regularTask, 0, 1000);

    }
}
