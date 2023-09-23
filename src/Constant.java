import java.util.regex.Pattern;

public interface Constant {
    String ApiUrl = "https://api.bilibili.com/x/web-interface/view?bvid=";
    String DEFAULT_TEMP_FILE_DIR = ".\\temp";
    int ALLOW_ERROR = 10;
    Pattern BvPattern = Pattern.compile("BV(\\w+)(?=/|$)");
    Pattern StringPattern = Pattern.compile("/([A-Za-z0-9]+)/\\?");
}
