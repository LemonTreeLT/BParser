package com.lemontree;

import java.util.Arrays;
import java.util.function.Predicate;

public class BParser {
    private static final Logger logger = new Logger(Logger.LoggerLevel.ALL, "BParser");
    public static void main(String[] args) {
        if(Arrays.stream(args).anyMatch(Predicate.isEqual("--tray"))) BParserWithTray.run(logger);
        else BParserOnTerminal.run(logger);
    }
}
