package me.thamma.quickbans;

import org.xml.sax.SAXException;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, SAXException, BrokenCacheException {
        // load variables from config
//        ConfigHandler ch = new ConfigHandler();
//        String league = ch.getValue("league");
//        int n = Integer.parseInt(ch.getValue("limit"));
//        String preset = ch.getValue("customPreset");
//        Hookup.getChamps("Elite Polyptoton", "EUW", 5).forEach(s ->
//                System.out.println(s));
        CacheHandler ch = new CacheHandler();
        System.out.println(ch.get("thm","EUW", "PERSONAL", 23));
        System.out.println(ch.get("Elite Polyptoton","EUW", "NORMAL", 6));

    }
}
