package me.thamma.quickbans;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.ElementType;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, SAXException {
        // load variables from config
//        ConfigHandler ch = new ConfigHandler();
//        String league = ch.getValue("league");
//        int n = Integer.parseInt(ch.getValue("limit"));
//        String preset = ch.getValue("customPreset");
        getChamps("Shaiin", "EUW", 3).forEach(s->
                System.out.println(s));
    }

    public static List<String> getChamps(String summoner, String region) throws IOException {
        List<String> champs = new ArrayList<String>();
        Document doc = Jsoup.connect("http://www.bestbans.com/summoner/" + region + "/" + summoner + "/season/").timeout(0).get();
        Elements champSquares = doc.getElementsByClass("summoner-panel").get(0).getElementsByClass("panel-body").get(0).getElementsByClass("row").get(0).getElementsByClass("summoner-champ-square");
        champSquares.forEach(square ->
                champs.add(square.getElementsByTag("p").get(0).text())
        );
        return champs;
    }

    public static List<String> getChamps(String summoner, String region, int amount) throws IOException {
        List<String> out = getChamps(summoner, region);
        return out.subList(0, Math.min(amount, out.size()));
    }

    public static List<String> getPersonalChamps(String summoner, String region) throws IOException {
        List<String> champs = new ArrayList<String>();
        Document doc = Jsoup.connect("http://www.bestbans.com/summoner/" + region + "/" + summoner + "/season/").timeout(0).get();
        Elements table = doc.getElementsByClass("col-md-9").get(0)
                .getElementById("summonerBansTable").getElementsByTag("tbody").get(0).getElementsByTag("tr");
        table.forEach(element -> champs.add(element.getElementsByTag("td").get(0).getElementsByTag("div").get(0).text()));
//        for (Element e : table) {
//            System.out.println(e.getElementsByTag("td").get(0).getElementsByTag("div").get(0).text());
//        }
//        System.out.println(table);
        return champs;
    }

    public static List<String> getPersonalChamps(String summoner, String region, int amount) throws IOException {
        List<String> out = getPersonalChamps(summoner, region);
        return out.subList(0, Math.min(amount, out.size()));
    }

    public static List<String> getGeneralChamps(String league) throws IOException, InterruptedException {
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
