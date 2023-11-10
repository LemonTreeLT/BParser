import java.io.File;
import java.text.SimpleDateFormat;

@SuppressWarnings("unused")
public class Logger {
    public enum LoggerLevel {
        ALL, DEBUG, ERROR, WARN, INFO
    }
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    private String Level;
    private String Prefix;
    private static final String LOG_DIR = ".\\log";

    public Logger(LoggerLevel LoggerLevel, String prefix) {
        Level = String.valueOf(LoggerLevel);
        this.Prefix = prefix;
        File file = new File(LOG_DIR);
        if (!file.exists()) if(!file.mkdir()) {
            System.out.println(Prefix("Error", getColorForLevel("error")) + "Can't creat log directory");
        }
    }

    public Logger(LoggerLevel loggerLevel) {
        this(loggerLevel, null);
    }

    public Logger() {
        this(LoggerLevel.ALL);
    }


    public void setLevel(LoggerLevel loggerLevel) {
        Level = String.valueOf(loggerLevel);
    }

    public void changePrefix(String prefixName) {
        Prefix = prefixName;
    }

    public void Error(String msg) {
        System.out.println(Prefix("ERROR", getColorForLevel("error")) + msg);
    }

    public void Warn(String msg) {
        if (Level.equals(String.valueOf(LoggerLevel.ERROR))) return;
        System.out.println(Prefix("WARN", getColorForLevel("warn")) + msg);
    }

    public void Info(String msg) {
        if (!Level.equals(String.valueOf(LoggerLevel.ALL)) && !Level.equals(String.valueOf(LoggerLevel.INFO))) return;
        System.out.println(Prefix("INFO", getColorForLevel("info")) + msg);
    }

    public void Debug(String msg) {
        if (!Level.equals(String.valueOf(LoggerLevel.DEBUG)) && !Level.equals(String.valueOf(LoggerLevel.ALL))) return;
        System.out.println(Prefix("DEBUG", getColorForLevel("debug")) + msg);
    }

    private String Prefix(String msg, String color) {
        // 时间前缀
        String PrefixTime = ConsoleColor.RESET + ConsoleColor.WHITE + "[" + ConsoleColor.GREEN +
                formatter.format(System.currentTimeMillis()) + ConsoleColor.WHITE + "]";

        // 等级前缀
        String PrefixLevel = PrefixTime + ConsoleColor.RESET + ConsoleColor.WHITE +
                "[" + ConsoleFont.BOLD + color + msg + ConsoleColor.RESET + ConsoleColor.WHITE + "] " + ConsoleColor.RESET;

        if (Prefix == null) return PrefixLevel;
        return ConsoleColor.WHITE + "[" + ConsoleColor.MAGENTA + this.Prefix + ConsoleColor.WHITE + "]" + PrefixLevel;
    }

    private String getColorForLevel(String level) {
        return switch(level.toLowerCase()) {
            case "warn" -> ConsoleColor.YELLOW;
            case "error" -> ConsoleColor.RED;
            case "debug" -> ConsoleColor.BLUE;
            default -> ConsoleColor.RESET;
        };
    }
    @SuppressWarnings("unused")
    private interface ConsoleColor {
        // 文字颜色
        String RESET = "\u001B[0m";
        String BLACK = "\u001B[30m";
        String RED = "\u001B[31m";
        String GREEN = "\u001B[32m";
        String YELLOW = "\u001B[33m";
        String BLUE = "\u001B[34m";
        String MAGENTA = "\u001B[35m";
        String CYAN = "\u001B[36m";
        String WHITE = "\u001B[37m";

        // 背景颜色
        String BG_BLACK = "\u001B[40m";
        String BG_RED = "\u001B[41m";
        String BG_GREEN = "\u001B[42m";
        String BG_YELLOW = "\u001B[43m";
        String BG_BLUE = "\u001B[44m";
        String BG_MAGENTA = "\u001B[45m";
        String BG_CYAN = "\u001B[46m";
        String BG_WHITE = "\u001B[47m";
    }

    @SuppressWarnings("unused")
    private interface ConsoleFont {
        // 字体样式
        String BOLD = "\u001B[1m";
        String ITALIC = "\u001B[3m";
        String UNDERLINE = "\u001B[4m";
        String INVERSE = "\u001B[7m";
    }
}




