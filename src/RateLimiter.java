import java.util.*;

/*
* A simple rate limiter for limiting API access
* */
public class RateLimiter {
    HashMap<String, ArrayList<Integer>> hits;
    public RateLimiter(){
        hits = new HashMap<>();
    }

    // increment is true if the request should be counted
    public boolean isRatelimited(int timestamp, String event, String rate, boolean increment) {
        if (!hits.containsKey(event)) {
            hits.put(event, new ArrayList<>());
        }
        String[] fields = rate.split("/");
        int rateLimit = Integer.parseInt(fields[0]);
        int limit = 0;
        String scale = fields[1];
        switch (scale) {
            case "s":
                limit = 1;
                break;
            case "m":
                limit = 60;
                break;
            case "h":
                limit = 3600;
                break;
            case "d":
                limit = 3600*24;
        }
        List<Integer> events = hits.get(event);
        int idx = events.size()-1;
        while (idx >= 0 && events.get(idx) > timestamp - limit) {
            idx--;
            rateLimit--;
            if (rateLimit == 0) {
                return true;
            }
        }
        if (increment) {
            events.add(timestamp);
        }
        return false;
    }

    public static void main(String[] args) {
        RateLimiter rl = new RateLimiter();
        System.out.println(rl.isRatelimited(1, "login", "3/m", true));
        System.out.println(rl.isRatelimited(11, "login", "3/m", true));
        System.out.println(rl.isRatelimited(21, "login", "3/m", true));
        System.out.println(rl.isRatelimited(30, "login", "3/m", true));
    }
}
