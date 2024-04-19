import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Timer;

public class BParserWithTray {
    public static void run(Logger logger) {
        TrayIcon icon = null;
        try {
            SystemTray tray = SystemTray.getSystemTray();

            //设置任务栏图标
            Image image = Toolkit.getDefaultToolkit().createImage(Constant.ICON_PATH);
            icon = getTrayIcon(image, logger);
            tray.add(icon);

            logger.Info("初始化任务栏成功");

        } catch(AWTException e) {
            logger.Error("无法初始化任务栏图标");
            System.exit(-1);
        }

        Utils utils = new Utils(logger, icon);
        Timer timer = new Timer();
        utils.introduce();

        timer.schedule(utils.regularTask, 0, 1000);
    }

    private static @NotNull TrayIcon getTrayIcon(Image image, Logger logger) {
        TrayIcon icon = new TrayIcon(image, "BParser");
        icon.setImageAutoSize(true);

        //添加菜单
        PopupMenu menu = new PopupMenu();
        MenuItem exitItem = new MenuItem("Exit");
        menu.add(exitItem);
        menu.setFont(new Font("微软雅黑", Font.PLAIN, 12));

        exitItem.addActionListener(event -> {
            logger.Info("退出程序");
            System.exit(0);
        });

        icon.setPopupMenu(menu);
        return icon;
    }
}
