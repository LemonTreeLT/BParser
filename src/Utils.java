import com.alibaba.fastjson2.JSONObject;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;

public class Utils {

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Logger logger = BParser.logger;
    public static String Search(String url) {
        Matcher matcher = Constant.StringPattern.matcher(url);
        if(!matcher.find()) return null;
        else {
            String bvid = matcher.group();
            Matcher BvID = Constant.BvPattern.matcher(bvid);
            if (!BvID.find()) return null;
            return BvID.group();
        }
    }

    public static JSONObject request(String urlString, String videoID) throws IOException {
        URL url = new URL(urlString + videoID);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if(responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String apiData = response.toString();
            return JSONObject.parseObject(apiData);
        } else {
            System.out.println("API请求失败，响应码：" + responseCode);
        }

        connection.disconnect();

        return null;
    }

    /**
     * 警告: 下载文件的格式只能通过修改源码修改
     * @param httpUrl 请求的网络链接
     * @param dir 下载目录
     * @param fileName 下载的文件名字
     * @return 成功与否
     * @throws IOException 正常报错
     */
    public static boolean DownlandFile(String httpUrl, String dir, String fileName) throws IOException {
        File ImageDir = new File(dir);
        URL url = new URL(httpUrl);
        URLConnection conn = url.openConnection();

        int byteRead;
        if (!ImageDir.exists()) if (!ImageDir.mkdir()) return false;

        InputStream inStream = conn.getInputStream();
        try(FileOutputStream fs = new FileOutputStream((dir + "\\" + fileName + ".jpg"))) {
            byte[] buffer = new byte[1204];

            while((byteRead = inStream.read(buffer)) != -1) fs.write(buffer, 0, byteRead);
        }
        return true;
    }

    public static boolean isValidURL(String text) {
        try {
            new URL(text);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public static String getVideoInfo(String url) {
        if (!isValidURL(url)) {
            logger.Info("非bilibili视频url");
            return null;
        }

        logger.Info("获取链接 " + url.replaceAll("/n", ""));
        String bvid = Utils.Search(url);
        if(bvid == null) {
            logger.Warn("无法解析到BvId");
            return null;
        } else logger.Info("解析到BvId: " + bvid);

        logger.Info("发送请求: " + Constant.ApiUrl + bvid);

        long startTime = System.currentTimeMillis();
        JSONObject jsonObject;
        try {
            jsonObject = Utils.request(Constant.ApiUrl, bvid);
        } catch(IOException e) {

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

        logger.Info("已获取视频信息: " + BVData.get("title"));

        String PicUrl = BVData.get("pic").toString();
        if(PicUrl == null) logger.Warn("无法获取图片链接");
        else logger.Info("获取图片链接: " + PicUrl);

        try {
            if(DownlandFile(PicUrl, Constant.DEFAULT_TEMP_FILE_DIR, bvid)) logger.Info("成功获取图片");
            else logger.Error("未知错误,无法获取图片,请检查你的网络连接");
        } catch(Exception e) {
            logger.Error("致命错误,位于: " + e.getMessage());
            return null;
        }

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
                "{image.getdata()}", BVData.get("title"),
                format.format((long) (int) BVData.get("pubdate") * 1000),
                JSONObject.parseObject(BVData.get("owner").toString()).get("name"),
                VideoStat.get("reply"), VideoStat.get("favorite"), VideoStat.get("coin"),
                VideoStat.get("like"), bvid
        );
    }
}
