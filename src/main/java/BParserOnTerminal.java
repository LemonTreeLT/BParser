import java.util.Timer;

public class BParserOnTerminal {
    public static void run(Logger logger) {
        Utils utils = new Utils(logger);
        utils.introduce();

        Timer timer = new Timer();
        timer.schedule(utils.regularTask, 0, 1000);
    }
}
