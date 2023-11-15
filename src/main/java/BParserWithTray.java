import java.awt.*;

public class BParserWithTray {
    private static final Logger logger = new Logger(Logger.LoggerLevel.ALL);
    private static final Utils utils = new Utils(logger);
    private static final SystemTray TRAY = SystemTray.getSystemTray();

    private static void initTray(SystemTray tray) throws AWTException {
        Image image = Toolkit.getDefaultToolkit().createImage(Constant.ICON_PATH);
        TrayIcon icon = new TrayIcon(image, "BParser");
        icon.setImageAutoSize(true);

        tray.add(icon);
    }

    public static void main(String[] args) {
        utils.introduce();
        try {
            initTray(TRAY);
        } catch(AWTException e) {
            logger.Error("无法初始化任务栏图标");
            System.exit(-1);
        }
    }
}
