package me.thamma.quickbans;

import java.awt.*;
import java.awt.List;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.util.*;

/**
 * Created by Dominic on 3/24/2016.
 */
public class Utils {

    public static void loadToClipboard(String s) {
        StringSelection selection = new StringSelection(s);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

    public static java.util.List<String> loadFile(String subpath) {
        java.util.List<String> lines = new ArrayList<String>();
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

    public static boolean saveFile(String subpath, java.util.List<String> lines) {
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
