package TinyURL;

import java.util.HashMap;

public class TinyURL {
    int lastShortUrlId;
    HashMap<String, Integer> longToId;
    HashMap<Integer, String> idToLong;
    char[] characters;
    HashMap<Character, Integer> charMap;
    HashMap<String, String> customizedToShortUrl;
    static final String startWith = "http://tiny.url/";

    public TinyURL() {
        lastShortUrlId = 0;
        longToId = new HashMap<>();
        idToLong = new HashMap<>();
        characters = new char[61];
        charMap = new HashMap<>();
        customizedToShortUrl = new HashMap<>();
        char start = 'a';
        for (int i = 0; i < 26; i++) {
            characters[i] = start;
            charMap.put(start, i);
            start++;
        }
        start = 'A';
        for (int i = 26; i < 52; i++) {
            characters[i] = start;
            charMap.put(start, i);
            start++;
        }
        start = '1';
        for (int i = 52; i < characters.length; i++) {
            characters[i] = start;
            charMap.put(start, i);
            start++;
        }
    }

    /*
     * @param long_url: a long url
     * @param key: a short key
     * @return: a short url starts with http://tiny.url/
     */
    public String createCustom(String long_url, String key) {
        if (customizedToShortUrl.containsKey(key)) {
            return "error";
        }
        if (!longToId.containsKey(long_url)) {
            int new_id = getNextId();
            longToId.put(long_url, new_id);
            idToLong.put(new_id, long_url);
        }
        String shortUrl = idToShortUrl(longToId.get(long_url));
        customizedToShortUrl.put(key, shortUrl);
        return startWith + key;
    }

    /*
     * @param url: a long url
     * @return: a short url starts with http://tiny.url/
     */
    public String longToShort(String url) {
        if (!longToId.containsKey(url)) {
            int new_id = getNextId();
            longToId.put(url, new_id);
            idToLong.put(new_id, url);
        }
        String shortUrl = idToShortUrl(longToId.get(url));

        return startWith + "0".repeat(Math.max(0, 6 - shortUrl.length())) + shortUrl;
    }

    /*
     * @param url: a short url starts with http://tiny.url/
     * @return: a long url
     */
    public String shortToLong(String url) {
        String[] fields = url.split(startWith);
        if (fields.length > 1) {
            url = fields[1];
        }
        if (customizedToShortUrl.containsKey(url)) {
            url = customizedToShortUrl.get(url);
        }
        int id = shortUrlToId(url);
        return idToLong.get(id);
    }

    private int getNextId() {
        lastShortUrlId++;
        while (customizedToShortUrl.containsKey(idToShortUrl(lastShortUrlId))) {
            lastShortUrlId++;
        }
        return lastShortUrlId;
    }

    private String idToShortUrl(int id) {
        StringBuilder sb = new StringBuilder();
        while (id > 0) {
            char last = characters[id % 61];
            sb.append(last);
            id /= 61;
        }
        return sb.toString();
    }

    private int shortUrlToId(String shortUrl) {
        int res = 0;
        for (int i = shortUrl.length()-1; i >= 0 && shortUrl.charAt(i) != '0'; i--) {
            res *= 61;
            res += charMap.get(shortUrl.charAt(i));
        }
        return res;
    }

    public static void main(String[] args) {
        TinyURL app = new TinyURL();
        String shortUrl = app.createCustom("www.vacation-planner.com", "vp");
        System.out.println(shortUrl);
        assert app.shortToLong(app.createCustom("www.google.com", "google")).equals("google");
    }
}
