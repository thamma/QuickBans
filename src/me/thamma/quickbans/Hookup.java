package me.thamma.quickbans;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Dominic on 3/24/2016.
 */
public class Hookup {

    public static List<String> getChamps(String summoner, String region) {
        List<String> champs = new ArrayList<String>();
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.bestbans.com/summoner/" + region + "/" + summoner + "/season/").timeout(0).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements champSquares = doc.getElementsByClass("summoner-panel").get(0).getElementsByClass("panel-body").get(0).getElementsByClass("row").get(0).getElementsByClass("summoner-champ-square");
        champSquares.forEach(square ->
                champs.add(square.getElementsByTag("p").get(0).text())
        );
        return champs;
    }

    public static List<String> getChamps(String summoner, String region, int amount) {
        List<String> out = getChamps(summoner, region);
        return out.subList(0, Math.min(amount, out.size()));
    }

    public static List<String> getPersonalChamps(String summoner, String region) {
        List<String> champs = new ArrayList<String>();
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.bestbans.com/summoner/" + region + "/" + summoner + "/season/").timeout(0).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements table = doc.getElementsByClass("col-md-9").get(0)
                .getElementById("summonerBansTable").getElementsByTag("tbody").get(0).getElementsByTag("tr");
        table.forEach(element -> champs.add(element.getElementsByTag("td").get(0).getElementsByTag("div").get(0).text()));
        return champs;
    }

    public static List<String> getPersonalChamps(String summoner, String region, int amount) {
        List<String> out = getPersonalChamps(summoner, region);
        return out.subList(0, Math.min(amount, out.size()));
    }

    public static String buildPipeString(Set<String> champsSet) {
        return String.join("|", champsSet);
    }

}
