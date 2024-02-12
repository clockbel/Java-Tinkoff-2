package edu.java.bot.model.url_utils;

import java.net.URL;
import java.util.Objects;

public final class ParseURL {
    private ParseURL() {
    }

    public static boolean parseURL(String inputURL) {
        String checkURL = CheckURL.checkURL(inputURL);
        boolean check = false;
        if (Objects.equals(checkURL, "OK")) {
            try {
                URL url = new URL(inputURL);
                check = true;
            } catch (Exception ignored) {
            }
        }
        return check;
    }
}
