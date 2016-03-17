package me.jesko;

import jdk.nashorn.internal.runtime.regexp.joni.Config;

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
        ConfigHandler ch = new ConfigHandler();
        String league = ch.getValue("league");
        int amount = Integer.parseInt(ch.getValue("limit"));
        Set<String> champs = new HashSet<String>();
        for (String division : league.split(",")) {
            champs.addAll(getChamps(division).subList(0, amount - 1));
        }
        List<String> noMultiples = new ArrayList<String>();
        noMultiples.addAll(champs);
        String out = buildPipeString(noMultiples);
        clipboard(out);
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

    public static List<String> parseChamps(List<String> htmlLines) throws InterruptedException {
        List<String> out = new ArrayList<String>();
        htmlLines = trim(htmlLines);
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
            if (content && !whiteSpace(temp))
                newLines.add(temp);
        }
        newLines.remove(0);
        return newLines;
    }

    public static boolean whiteSpace(String s) {
        for (char c : s.toCharArray()) {
            if (c != ' ')
                return false;
        }
        return true;
    }

    public static String buildPipeString(List<String> champs) {
        String out = "";
        for (int i = 0; i < champs.size(); i++) {
            out += (i != 0 ? "|" : "") + champs.get(i);
        }
        return out;
    }

    public static void clipboard(String theString) {
        StringSelection selection = new StringSelection(theString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }


}
