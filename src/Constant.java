import java.util.regex.Pattern;

public interface Constant {
    String ApiUrl = "https://api.bilibili.com/x/web-interface/view?bvid=";
    String DEFAULT_TEMP_FILE_DIR = ".\\img";
    int ALLOW_ERROR = 10;
    Pattern BvPattern = Pattern.compile("BV(\\w+)(?=/|$)");
    Pattern StringPattern = Pattern.compile("/([A-Za-z0-9]+)/\\?");
    String IntroduceBParser = """
               ___      ___                                         \s
              | _ )    | _ \\  __ _      _ _    ___     ___      _ _ \s
              | _ \\    |  _/ / _` |    | '_|  (_-<    / -_)    | '_|\s
              |___/   _|_|_  \\__,_|   _|_|_   /__/_   \\___|   _|_|_ \s
            _|""\"""|_| ""\" |_|""\"""|_|""\"""|_|""\"""|_|""\"""|_|""\"""|\s
            "`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'""";
}
