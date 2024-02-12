package edu.java.bot.model.command_utils.checks;

import java.net.URL;
import java.util.List;

public final class CheckTrack {
    private CheckTrack() {
    }

    public static boolean checkURLInBase(List<URL> urlList, URL url) {
        boolean check = true;
        if (!urlList.contains(url)) {
            check = false;
        }
        return check;
    }
}
