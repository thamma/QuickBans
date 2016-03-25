package me.thamma.quickbans;

import java.util.*;

public class CacheHandler {

    final String cachePath = System.getenv("LOCALAPPDATA") + "\\QuickBans\\cache.csv";

    final long requestTime = 1000 * 60 * 60 * 8;
    private List<String> rawLocal;
    private Map<String, String> local;

    public CacheHandler() throws BrokenCacheException {
        loadCache();

    }

    private void loadCache() throws BrokenCacheException {
        this.local = new HashMap<>();
        this.rawLocal = Utils.loadFile(cachePath);
        for (String s : rawLocal) {
            if (s.split(",").length != 5)
                throw new BrokenCacheException();
            String summoner = s.split(",")[0];
            String region = s.split(",")[1];
            String mode = s.split(",")[2];
            String lastUpdate = s.split(",")[3];
            String value = s.split(",")[4];
            local.put(summoner + "," + region + "," + mode, lastUpdate + "," + value);
        }
    }

    private void saveCache() {
        this.rawLocal = new ArrayList<>();
        for (String key : this.local.keySet()) {
            String summoner = key.split(",")[0];
            String region = key.split(",")[1];
            String mode = key.split(",")[2];
            String lastUpdate = this.local.get(key).split(",")[0];
            String value = this.local.get(key).split(",")[1];
            rawLocal.add(summoner + "," + region + "," + mode + "," + lastUpdate + "," + value);
        }
        Utils.saveFile(cachePath, rawLocal);
    }

    // summoner,region,mode,lastUpdate,value
    // summoner,region,mode -> lastUpdate,value

    public String get(String summoner, String region, String mode, int amount) throws BrokenCacheException {
        if (this.local.containsKey(summoner + "," + region + "," + mode)) {
            String cached = local.get(summoner + "," + region + "," + mode);
            if (cached.split(",").length != 2)
                throw new BrokenCacheException();

            long lastUpdate = Long.parseLong(cached.split(",")[0]);

            if (System.currentTimeMillis() - lastUpdate > requestTime) {
                return reduce(request(summoner, region, mode, amount), amount);
            } else {
                return reduce(this.local.get(summoner + "," + region + "," + mode), amount);
            }
        }
        return reduce(request(summoner, region, mode, amount), amount);
    }

    public String request(String summoner, String region, String mode, int amount) {
        System.out.println("ISSUED_REQUEST{" + summoner + "," + region + "," + mode + "}");
        if (mode.equalsIgnoreCase("PERSONAL")) {
            String value = String.join("|", Hookup.getPersonalChamps(summoner, region));
            this.local.put(summoner + "," + region + "," + mode, System.currentTimeMillis() + "," + value);
            saveCache();
            return value;
        } else {
            String value = String.join("|", Hookup.getChamps(summoner, region));
            this.local.put(summoner + "," + region + "," + mode, System.currentTimeMillis() + "," + value);
            saveCache();
            return value;
        }
    }

    public String reduce(String s, int cap) {
        String[] in = s.split("\\|");
        String[] out = Arrays.copyOf(in, Math.min(cap,in.length));
        return String.join("|", out);
    }

}
