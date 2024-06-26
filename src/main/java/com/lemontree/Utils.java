package com.lemontree;

import com.alibaba.fastjson2.JSONObject;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.*;
import java.net.*;
import java.net.http.HttpConnectTimeoutException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.UnexpectedException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final Logger logger;
    private TrayIcon icon;
    private String BvID = "";
    private long usedTime;

    public Utils(Logger logger) {
        this(logger, null);
    }

    public Utils(Logger logger, TrayIcon icon) {
        this.logger = logger;
        this.icon = icon;
    }

    public Runnable regularTask = new TimerTask() {
        String clipboardPast = null;

        @Override
        public void run() {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            try {

                Transferable contents = clipboard.getContents(null);
                if(contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor) &&
                        !Objects.equals(clipboardPast, contents.getTransferData(DataFlavor.stringFlavor))) {

                    String copiedText = (String) contents.getTransferData(DataFlavor.stringFlavor);
                    Transferable parserContent = null;

                    try {
                        parserContent = getVideoInfo(copiedText);
                    } catch(IOException e) {
                        logger.Error("发生错误,位于: " + e.getMessage());
                    }

                    if(parserContent != null) {
                        clipboard.setContents(parserContent, null);
                        if(icon != null) icon.displayMessage("BParser",
                                String.format("解析视频%s 用时%sms", BvID, usedTime),
                                TrayIcon.MessageType.INFO);
                    }
                    contents = clipboard.getContents(null);
                    clipboardPast = (String) contents.getTransferData(DataFlavor.stringFlavor);

                }
            } catch(IOException | UnsupportedFlavorException e) {
                logger.Error("发生未知错误: " + e.getMessage());
            }
        }
    };

    public void setIcon(TrayIcon icon) {
        this.icon = icon;
    }

    public String Search(String url, Pattern pattern) {
        Matcher matcher = pattern.matcher(url);
        if(!matcher.find()) return null;
        else {
            String bvid = matcher.group();
            Matcher BvID = Constant.BvPattern.matcher(bvid);
            if(!BvID.find()) return null;
            return BvID.group();
        }
    }

    /**
     * api请求器
     *
     * @param urlString Api链接
     * @param videoID   视频链接
     * @return 视频信息
     * @throws IOException 文件系统错误
     */
    public JSONObject request(String urlString, String videoID) throws IOException {
        URL url = new URL(urlString + videoID);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Charset", "UTF-8");
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if(responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null) response.append(line);
            reader.close();

            String apiData = response.toString();
            connection.disconnect();
            return JSONObject.parseObject(apiData);
        }
        connection.disconnect();
        throw new IOException("1");
    }

    /**
     * 这是为这个项目添加的介绍,不建议复制
     */
    public void introduce() {
        System.out.println(Constant.IntroduceBParser);
        System.out.println("                                     \u001B[3mby LemonTree\u001B[0m");
        System.out.println("================================================================");
    }

    public String downloadFile(URL url) throws IOException {
        String temp = System.getProperty("java.io.tmpdir");
        Path bParserDirPath = Paths.get(temp, "BParser");
        Files.createDirectories(bParserDirPath);

        String fileName = url.getFile().substring(url.getFile().lastIndexOf('/') + 1);
        Path targetFilePath = Paths.get(bParserDirPath.toString(), fileName);

        try(InputStream in = new BufferedInputStream(url.openStream());
            OutputStream out = new FileOutputStream(targetFilePath.toFile())) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while((bytesRead = in.read(buffer)) != -1) out.write(buffer, 0, bytesRead);
            out.flush();
        }
        return targetFilePath.toUri().toString();
    }

    public String strToSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for(byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(NoSuchAlgorithmException e) {
            logger.Error("发生错误: " + e);
            return "Error";
        }
    }

    public Transferable getVideoInfo(String url) throws IOException {
        logger.Info("获取剪切板 | S:" + url.length() + " | " + strToSHA256(url));
        BvID = Search(url, Constant.StringPattern);

        if(BvID == null) return null;
        else logger.Video("解析到BvId: " + BvID);
        long startTime = System.currentTimeMillis();

        JSONObject jsonObject;
        jsonObject = request(Constant.ApiUrl, BvID);

        if(jsonObject == null) throw new HttpConnectTimeoutException("2");

        JSONObject BVData = JSONObject.parseObject(jsonObject.get("data").toString());
        JSONObject VideoStat = JSONObject.parseObject(BVData.get("stat").toString());
        String VideoTitle = (String) BVData.get("title");
        String PicUrl = BVData.get("pic").toString();

        if(PicUrl == null) logger.Warn("无法获取图片链接");
        else PicUrl = downloadFile(new URL(PicUrl));

        String wholeInfo = String.format(
                "%s<br>发布时间: %s<br>up: %s<br>评论数: %s<br>收藏数: %s<br>硬币数: %s<br>点赞数: %s<br>https://www.bilibili.com/video/%s ",
                VideoTitle, format.format((long) (int) BVData.get("pubdate") * 1000),
                JSONObject.parseObject(BVData.get("owner").toString()).get("name"),
                VideoStat.get("reply"), VideoStat.get("favorite"), VideoStat.get("coin"),
                VideoStat.get("like"), BvID
        );

        usedTime = System.currentTimeMillis() - startTime;
        try {
            Transferable vidInfo = new FormatInfo(PicUrl, wholeInfo);
            logger.Video("构建视频信息成功, 用时: " + usedTime + "ms");
            return vidInfo;
        } catch(Exception e) {
            throw new UnexpectedException("构建视频信息失败, 用时: " + usedTime + "ms", e);
        }
    }
}
