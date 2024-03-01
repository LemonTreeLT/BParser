import com.alibaba.fastjson2.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final Logger logger;

    public Utils(Logger logger) {
        this.logger = logger;
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
        logger.Debug(String.valueOf(responseCode));
        connection.disconnect();
        throw new IOException();
    }

    /**
     * 这是为这个项目添加的介绍,不建议复制
     */
    public void introduce() {
        System.out.println(Constant.IntroduceBParser);
        System.out.println("                                     \u001B[3mby LemonTree\u001B[0m");
        System.out.println("================================================================");
    }

    public String strToSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for(byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if(hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(NoSuchAlgorithmException e) {
            logger.Error("发生错误: " + e);
            return "Error";
        }
    }

    public String getVideoInfo(String url) {
        logger.Info("获取剪切板 | S:" + url.length() + " | " + strToSHA256(url));
        String bvId = Search(url, Constant.StringPattern);
        if(bvId == null) return null;
        else logger.Info("解析到BvId: " + bvId);

        logger.Info("发送请求: " + Constant.ApiUrl + bvId);

        long startTime = System.currentTimeMillis();
        JSONObject jsonObject;
        try {
            jsonObject = request(Constant.ApiUrl, bvId);
        } catch(IOException e) {
            logger.Error("Api请求失败,请检查你的网络链接 错误位于: " + e);
            return null;
        }
        long endTime = System.currentTimeMillis();

        logger.Info("请求用时" + (endTime - startTime) + "ms");
        if(jsonObject == null) {
            logger.Error("Api请求失败,请检查你的网络链接");
            return null;
        }

        JSONObject BVData = JSONObject.parseObject(jsonObject.get("data").toString());
        JSONObject VideoStat = JSONObject.parseObject(BVData.get("stat").toString());
        String VideoTitle = (String) BVData.get("title");

        logger.Info("已获取视频信息: " + VideoTitle);

        String PicUrl = BVData.get("pic").toString();
        if(PicUrl == null) logger.Warn("无法获取图片链接");
        else logger.Info("获取图片链接: " + PicUrl);
        logger.Info("停止获取图片");

        return String.format(
                """
                        %s #芝士图片
                        %s
                        发布时间: %s
                        up: %s
                        评论数: %s
                        收藏数: %s
                        硬币数: %s
                        点赞数: %s
                        https://www.bilibili.com/video/%s
                        """,
                "NoPIC", VideoTitle,
                format.format((long) (int) BVData.get("pubdate") * 1000),
                JSONObject.parseObject(BVData.get("owner").toString()).get("name"),
                VideoStat.get("reply"), VideoStat.get("favorite"), VideoStat.get("coin"),
                VideoStat.get("like"), bvId
        );
    }
}
