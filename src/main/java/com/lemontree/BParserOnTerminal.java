package com.lemontree;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BParserOnTerminal {
    public static void run(Logger logger) {
        Utils utils = new Utils(logger);
        utils.introduce();

        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(utils.regularTask, 0, 2, TimeUnit.SECONDS);
    }
}
