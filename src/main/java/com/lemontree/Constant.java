package com.lemontree;

import java.util.regex.Pattern;

public interface Constant {
    String videoInfoApiUrl = "https://api.bilibili.com/x/web-interface/view?bvid=";
    String pageInfoApiUrl = "https://api.bilibili.com/x/player/pagelist?bvid=";
    Pattern BvPattern = Pattern.compile("/video/(BV[0-9A-Za-z]{10})(?:[/?]|$)");
    Pattern pagePattern = Pattern.compile("[?&]p=(\\d+)(?:&|$)");
    String IntroduceBParser = """
               ___      ___                                         \s
              | _ )    | _ \\  __ _      _ _    ___     ___      _ _ \s
              | _ \\    |  _/ / _` |    | '_|  (_-<    / -_)    | '_|\s
              |___/   _|_|_  \\__,_|   _|_|_   /__/_   \\___|   _|_|_ \s
            _|""\"""|_| ""\" |_|""\"""|_|""\"""|_|""\"""|_|""\"""|_|""\"""|\s
            "`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'""";
}
