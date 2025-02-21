package com.lemontree;

import com.alibaba.fastjson2.JSONObject;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.rmi.UnexpectedException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
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

    public class Monitor extends TimerTask {
        private String lastCheckedString = null;

        @Override
        public void run() {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            try {
                String currentString = getClipBoardString(clipboard);
                if(currentString != null && !currentString.equals(lastCheckedString)) {
                    lastCheckedString = currentString;
                    Transferable parserContent = parseVideoInfo(currentString);
                    if(parserContent != null) {
                        clipboard.setContents(parserContent, null);
                        displayMessage();
                        try {
                            lastCheckedString = getClipBoardString(clipboard);
                        } catch(Exception e) {
                            // Keep lastCheckedString as the original string
                        }
                    }
                }
            } catch(Exception e) {
                logger.Error("Error: " + e.getMessage());
            }
        }

        private String getClipBoardString(Clipboard clipboard) throws IOException, UnsupportedFlavorException {
            Transferable contents = clipboard.getContents(null);
            if(contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return (String) contents.getTransferData(DataFlavor.stringFlavor);
            }
            return null;
        }

        private Transferable parseVideoInfo(String text) {
            try {
                return getVideoInfo(text);
            } catch(IOException e) {
                logger.Error("Error parsing video info: " + e.getMessage());
                return null;
            } catch(InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private void displayMessage() {
            if(icon == null) return;
            icon.displayMessage("BParser",
                    String.format("Parsed video %s in %s ms", BvID, usedTime),
                    TrayIcon.MessageType.INFO);
        }
    }

    public Runnable clipMonitor = new Monitor();

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
     * @throws HttpConnectTimeoutException 错误
     */
    public JSONObject request(String urlString, String videoID) throws IOException, InterruptedException {
        URI uri = URI.create(urlString + videoID);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().header("Accept-Charset", "UTF-8").build();

        HttpResponse<String> response;
        try(HttpClient client = HttpClient.newHttpClient()) {
            response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if(response.statusCode() == 200) {
                String apiData = response.body();
                return JSONObject.parseObject(apiData);
            } else throw new HttpConnectTimeoutException("请求失败，状态码: " + response.statusCode());
        }
    }

    /**
     * 这是为这个项目添加的介绍,不建议复制
     */
    public void introduce() {

        System.out.println(Constant.IntroduceBParser);
        System.out.println("                                     \u001B[3mby LemonTree\u001B[0m");
        System.out.println("================================================================");
    }

    public String downloadFile(String urlString) throws IOException {
        URI uri = URI.create(urlString);
        String fileName = uri.getPath().substring(uri.getPath().lastIndexOf('/') + 1);
        String temp = System.getProperty("java.io.tmpdir");
        Path targetFilePath = Paths.get(temp, "BParser", fileName);

        Files.createDirectories(targetFilePath.getParent());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<InputStream> response;
        try(HttpClient client = HttpClient.newHttpClient()) {
            response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            if(response.statusCode() == 200) {
                try(InputStream in = response.body()) {
                    Files.copy(in, targetFilePath, StandardCopyOption.REPLACE_EXISTING);
                }
                return targetFilePath.toUri().toString();
            } else {
                throw new IOException("下载文件失败，状态码：" + response.statusCode());
            }
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
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

    public Transferable getVideoInfo(String url) throws IOException, InterruptedException {
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
        String wholeInfo = String.format(
                "%s<br>发布时间: %s<br>up: %s<br>评论数: %s<br>收藏数: %s<br>硬币数: %s<br>点赞数: %s<br>https://www.bilibili.com/video/%s ",
                VideoTitle, format.format((long) (int) BVData.get("pubdate") * 1000),
                JSONObject.parseObject(BVData.get("owner").toString()).get("name"),
                VideoStat.get("reply"), VideoStat.get("favorite"), VideoStat.get("coin"),
                VideoStat.get("like"), BvID
        );

        usedTime = System.currentTimeMillis() - startTime;
        try {
            if(PicUrl == null) logger.Warn("无法获取图片链接");
            Transferable vidInfo = PicUrl == null?
                    new FormatInfo(null, wholeInfo): new FormatInfo(downloadFile(PicUrl), wholeInfo);
            logger.Video("构建视频信息成功, 用时: " + usedTime + "ms");
            return vidInfo;
        } catch(Exception e) {
            throw new UnexpectedException("构建视频信息失败, 用时: " + usedTime + "ms", e);
        }
    }
}
