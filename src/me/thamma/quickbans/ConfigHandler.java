package me.thamma.quickbans;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Dominic on 3/16/2016.
 */
public class ConfigHandler {

    final String path = "quickbans.cfg";
    private List<String> lines;

    public ConfigHandler() {
        lines = loadFile(path);
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
                    "league=silver";

    private void writeDefaultConfig() {
        List<String> defConf = new ArrayList<String>();
        for (String s : defaultConfig.split("\\n"))
            defConf.add(s);
        this.lines = defConf;
        saveFile(path, defConf);
    }

    public static List<String> loadFile(String subpath) {
        List<String> lines = new ArrayList<String>();
        File f = new File(subpath);
        if (f.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
                br.close();
            } catch (IOException e) {
                System.out.println("Error loading file: \"" + e.getMessage()
                        + "\"");
            }

        } else {
            System.out.println("File \"" + subpath + "\" could not be found.");
        }
        return lines;
    }

    public static boolean saveFile(String subpath, List<String> lines) {
        File f = new File(subpath);
        f.mkdirs();
        if (f.exists()) {
            f.delete();
        }
        try {
            FileWriter writer;
            writer = new FileWriter(f);
            for (String s : lines) {
                writer.write(s + "\n");
            }
            writer.close();
            return true;
        } catch (IOException e) {
            System.out.println("Error saving file: \"" + e.getMessage() + "\"");
            return false;
        }
    }

}
