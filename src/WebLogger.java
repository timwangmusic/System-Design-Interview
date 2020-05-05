import java.util.Deque;
import java.util.LinkedList;

public class WebLogger {
    Deque<Integer> queue;

    public WebLogger() {
        queue = new LinkedList<>();
    }

    /*
     * @param timestamp: An integer
     * @return: nothing
     */
    public void hit(int timestamp) {
        queue.addLast(timestamp);
    }

    /*
     * @param timestamp: An integer
     * @return: An integer
     */
    public int get_hit_count_in_last_5_minutes(int timestamp) {
        while (queue.size() > 0 && queue.getFirst() <= timestamp - 300) {
            queue.removeFirst();
        }
        return queue.size();
    }
    public static void main(String[] args) {
        WebLogger webLogger = new WebLogger();
        webLogger.hit(100);
        webLogger.hit(200);
        System.out.println(webLogger.get_hit_count_in_last_5_minutes(300));
        System.out.println(webLogger.get_hit_count_in_last_5_minutes(3000));
    }
}
