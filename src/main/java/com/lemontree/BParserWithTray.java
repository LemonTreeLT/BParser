package com.lemontree;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class BParserWithTray {
    private static boolean isRunning = true;
    private static ScheduledFuture<?> scheduledFuture;

    public static void run(Logger logger) {
        try {
            SystemTray tray = SystemTray.getSystemTray();

            //设置任务栏图标
            Image image = Toolkit.getDefaultToolkit().createImage(BParser.class.getResource("icon.png"));
            Utils utils = new Utils(logger);
            TrayIcon icon = getTrayIcon(image, logger, utils);

            utils.setIcon(icon);
            tray.add(icon);

            logger.Info("初始化任务栏成功");

            utils.introduce();

            scheduledFuture = Executors.newSingleThreadScheduledExecutor()
                    .scheduleAtFixedRate(utils.clipMonitor, 0, 1, TimeUnit.SECONDS);

        } catch(AWTException e) {
            logger.Error("无法初始化任务栏图标");
            System.exit(-1);
        }
    }

    private static @NotNull TrayIcon getTrayIcon(Image image, Logger logger, Utils utils) {
        TrayIcon icon = new TrayIcon(image, "BParser");
        icon.setImageAutoSize(true);

        //添加菜单
        PopupMenu menu = new PopupMenu();
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem toggleItem = new MenuItem("Stop");
        MenuItem analyzeItem = new MenuItem("Analyze");
        analyzeItem.setEnabled(false);
        menu.add(exitItem);
        menu.add(toggleItem);
        menu.add(analyzeItem);
        menu.setFont(new Font("微软雅黑", Font.PLAIN, 12));

        exitItem.addActionListener(event -> {
            logger.Info("退出程序");
            System.exit(0);
        });

        analyzeItem.addActionListener(event -> {
            logger.Info("用户手动进行解析");
            utils.clipMonitor.run();
        });

        class ToggleState implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isRunning) {
                    toggleItem.setLabel("Run");
                    analyzeItem.setEnabled(true);
                    scheduledFuture.cancel(false);
                    logger.Info("停止程序");
                    isRunning = false;
                } else {
                    toggleItem.setLabel("Stop");
                    analyzeItem.setEnabled(false);
                    scheduledFuture = Executors.newSingleThreadScheduledExecutor()
                            .scheduleAtFixedRate(utils.clipMonitor, 0, 1, TimeUnit.SECONDS);
                    logger.Info("启动程序");
                    isRunning = true;
                }
            }
        }

        ToggleState toggleState = new ToggleState();
        toggleItem.addActionListener(toggleState);

        icon.setPopupMenu(menu);
        return icon;
    }
}
