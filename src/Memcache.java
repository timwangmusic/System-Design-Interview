import java.util.HashMap;

public class Memcache {
    HashMap<Integer, Integer> expiresAt;
    HashMap<Integer, Integer> cache;
    static final int MAX_INT = 2147483647;

    public Memcache() {
       expiresAt = new HashMap<>();
       cache = new HashMap<>();
    }

    /*
     * @param curtTime: An integer
     * @param key: An integer
     * @return: An integer
     */
    public int get(int curtTime, int key) {
        if (!cache.containsKey(key)) {
            return MAX_INT;
        }
        if (expiresAt.get(key) > 0 && curtTime >= expiresAt.get(key)) {
            cache.remove(key);
            return MAX_INT;
        }
        return cache.get(key);
    }

    /*
     * @param curtTime: An integer
     * @param key: An integer
     * @param value: An integer
     * @param ttl: An integer
     * @return: nothing
     */
    public void set(int curtTime, int key, int value, int ttl) {
        cache.put(key, value);
        if (ttl == 0) {
            expiresAt.put(key, 0);
        } else {
            expiresAt.put(key, curtTime+ttl);
        }
    }

    /*
     * @param curtTime: An integer
     * @param key: An integer
     * @return: nothing
     */
    public void delete(int key) {
        cache.remove(key);
    }

    /*
     * @param curtTime: An integer
     * @param key: An integer
     * @param delta: An integer
     * @return: An integer
     */
    public int incr(int curtTime, int key, int delta) {
        if (!cache.containsKey(key)) {
            return MAX_INT;
        }
        if (expiresAt.get(key) > 0 && expiresAt.get(key) <= curtTime) {
            cache.remove(key);
            return MAX_INT;
        }
        int newVal = cache.get(key)+delta;
        cache.put(key, newVal);
        return newVal;
    }

    /*
     * @param curtTime: An integer
     * @param key: An integer
     * @param delta: An integer
     * @return: An integer
     */
    public int decr(int curtTime, int key, int delta) {
        if (!cache.containsKey(key)) {
            return MAX_INT;
        }
        if (expiresAt.get(key) > 0 && expiresAt.get(key) <= curtTime) {
            cache.remove(key);
            return MAX_INT;
        }
        int newVal = cache.get(key)-delta;
        cache.put(key, newVal);
        return newVal;
    }

    public static void main(String[] args) {
        Memcache cache = new Memcache();
        cache.set(1, 10, 10*10, 0);
        System.out.println(cache.get(2,10));
        System.out.println(cache.get(20,10));
        System.out.printf("new value after increment is %d \n", cache.incr(30, 10, 100));
        System.out.println(cache.get(32,10));
        System.out.printf("new value after decrement is %d \n", cache.decr(40, 10, 100));
        System.out.println(cache.get(200,10));
        cache.delete(19);
    }
}
