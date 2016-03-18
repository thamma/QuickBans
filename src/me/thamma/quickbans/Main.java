package me.thamma.quickbans;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        // load variables from config
        ConfigHandler ch = new ConfigHandler();
        String league = ch.getValue("league");
        int n = Integer.parseInt(ch.getValue("limit"));
        // collect the data in Set
        Set<String> champs = new HashSet<String>();
        //add first n champs per league to set
        for (String division : league.split(","))
            champs.addAll(Main.getChamps(division).subList(0, n - 1));
        String clipboardContent = Main.buildPipeString(champs);
        Main.clipboard(clipboardContent);
        Toolkit.getDefaultToolkit().beep();
    }

    public static List<String> getChamps(String league) throws IOException, InterruptedException {
        List<String> lines = loadHtml(league);
        List<String> champs = parseChamps(lines);
        return champs;
    }

    private static List<String> loadHtml(String league) throws IOException {
        String adress = "http://bestbans.com/bestBans/" + league;
        URL url = new URL(adress);
        URLConnection urlConnection = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                urlConnection.getInputStream(), "UTF-8"));
        List<String> out = new ArrayList<String>();
        String inputLine;
        StringBuilder a = new StringBuilder();
        while ((inputLine = in.readLine()) != null)
            out.add(inputLine);
        in.close();
        return out;
    }

    public static List<String> parseChamps(List<String> htmlLines) {
        List<String> out = new ArrayList<String>();
        htmlLines = trim(htmlLines);
        // yes this is very messy and will break once the site layout changes
        for (int i = 0; i < htmlLines.size() / 13; i++) {
            out.add(htmlLines.get(i * 13 + 4).replaceAll("&#39;", "'").trim());
        }
        return out;
    }

    private static List<String> trim(List<String> htmlLines) {
        boolean content = false;
        List<String> newLines = new ArrayList<String>();
        for (String temp : htmlLines) {
            if (!content && temp.contains("tbody")) {
                content = true;
            }
            if (content && temp.contains("/tbody>")) {
                break;
            }
            if (content && !(temp.trim().equals("")))
                newLines.add(temp);
        }
        newLines.remove(0); // pop <tbody>
        return newLines;
    }


    public static String buildPipeString(Set<String> champsSet) {
        return String.join("|", champsSet);
    }

    public static void clipboard(String s) {
        StringSelection selection = new StringSelection(s);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }


}
