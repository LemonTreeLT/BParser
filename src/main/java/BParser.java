import java.util.Arrays;
import java.util.function.Predicate;

public class BParser {
    public static void main(String[] args) {
        enum Mode {Tray, Terminal}
        Mode mode;
        if(Arrays.stream(args).anyMatch(Predicate.isEqual("--tray"))) mode = Mode.Tray;
        else mode = Mode.Terminal;

        switch(mode) {
            case Tray -> BParserWithTray.run();
            case Terminal -> BParserOnTerminal.run();
        }
    }
}
