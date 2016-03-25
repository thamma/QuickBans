package me.thamma.quickbans;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Dominic on 3/16/2016.
 */
public class ConfigHandler {

    final String configPath = System.getenv("LOCALAPPDATA") + "\\quickbans\\quickbans.cfg";
    private List<String> lines;

    public ConfigHandler() {
        lines = Utils.loadFile(configPath);
    }

    public String getValue(String key) {
        for (String s : this.lines) {
            if (s.startsWith(key + "="))
                return s.replaceFirst(key + "=", "");
        }
        writeDefaultConfig();
        return getValue(key);
    }

    final String defaultConfig =
            "# The maximum number of champions to be suggested (recommended > 6 (per league!))\n" +
                    "limit=10\n" +
                    "# The league(s) to suggest the bans for\n" +
                    "# seperate multiple leagues by commas\n" +
                    "# options: bronze, silver, gold, platinum, diamond\n" +
                    "league=silver\n" +
                    "# You can also specify a very custom preset. If not, leave it blank.\n" +
                    "# (Beware: it will then ignore all the above options!)\n" +
                    "customPreset=";

    private void writeDefaultConfig() {
        List<String> defConf = new ArrayList<String>();
        for (String s : defaultConfig.split("\\n"))
            defConf.add(s);
        this.lines = defConf;
        Utils.saveFile(configPath, defConf);
    }

}
