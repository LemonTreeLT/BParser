package com.lemontree;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        TrayIcon icon;
        Utils utils = null;
        try {
            SystemTray tray = SystemTray.getSystemTray();

            //设置任务栏图标
            Image image = Toolkit.getDefaultToolkit().createImage(BParser.class.getResource("icon.png"));
            utils = new Utils(logger);
            icon = getTrayIcon(image, logger, utils);

            utils.setIcon(icon);
            tray.add(icon);

            logger.Info("初始化任务栏成功");

        } catch(AWTException e) {
            logger.Error("无法初始化任务栏图标");
            System.exit(-1);
        }
        utils.introduce();

        scheduledFuture = Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(utils.regularTask, 0, 1, TimeUnit.SECONDS);
    }

    private static @NotNull TrayIcon getTrayIcon(Image image, Logger logger, Utils utils) {
        TrayIcon icon = new TrayIcon(image, "BParser");
        icon.setImageAutoSize(true);
        class MPopupMenu extends JPopupMenu {

            public MPopupMenu() {
                super();
                setBorder(new EmptyBorder(10, 10, 10, 10));
                setBackground(Color.WHITE);
                setOpaque(true);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(getBackground());
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            }
        }


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
            utils.regularTask.run();
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
                            .scheduleAtFixedRate(utils.regularTask, 0, 1, TimeUnit.SECONDS);
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
