package TinyURL;

import java.util.HashMap;

public class TinyURL {
    int lastShortUrlId;
    HashMap<String, Integer> longToId;
    HashMap<Integer, String> idToLong;
    char[] characters;
    HashMap<Character, Integer> charMap;
    String startWith;

    public TinyURL() {
        startWith = "http://tiny.url/";
        lastShortUrlId = 0;
        longToId = new HashMap<>();
        idToLong = new HashMap<>();
        characters = new char[61];
        charMap = new HashMap<>();
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

    private int getNextShortUrl() {
        lastShortUrlId++;
        return lastShortUrlId;
    }

    private String idToShortUrl(int id) {
        if (id == 0) {
            return "a";
        }
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

    /*
     * @param url: a long url
     * @return: a short url starts with http://tiny.url/
     */
    public String longToShort(String url) {
        if (!longToId.containsKey(url)) {
            int new_id = getNextShortUrl();
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
        url = url.split(startWith)[1];
        int id = shortUrlToId(url);
        return idToLong.get(id);
    }

    public static void main(String[] args) {
        TinyURL app = new TinyURL();
        String shortUrl = app.longToShort("http://www.lintcode.com/faq/?id=10");
        String longUrl = app.shortToLong(shortUrl);
        System.out.println(longUrl);
    }
}
