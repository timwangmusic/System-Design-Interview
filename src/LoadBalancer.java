import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class LoadBalancer {
    HashMap<Integer, Integer> m;
    ArrayList<Integer> servers;

    public LoadBalancer() {
        m = new HashMap<>();
        servers = new ArrayList<>();
    }

    /*
     * @param server_id: add a new server to the cluster
     * @return: nothing
     */
    public void add(int server_id) {
        if (!m.containsKey(server_id)) {
            servers.add(server_id);
            m.put(server_id, servers.size()-1);
        }
    }

    /*
     * @param server_id: server_id remove a bad server from the cluster
     * @return: nothing
     */
    public void remove(int server_id) {
        if (m.containsKey(server_id)) {
            Integer idx = m.get(server_id);
            Integer val = servers.get(servers.size()-1);
            m.put(val, idx);
            swap(idx, servers.size()-1);
            servers.remove(servers.size()-1);
            m.remove(server_id);
        }
    }

    public void swap(Integer x, Integer y) {
        Integer tmp = servers.get(y);
        servers.set(y, servers.get(x));
        servers.set(x, tmp);
    }

    /*
     * @return: pick a server in the cluster randomly with equal probability
     */
    public int pick() {
        if (servers.size() == 0) {
            return -1;
        }
        Random r = new Random();
        return servers.get(r.nextInt(servers.size()));
    }

    public static void main(String[] args) {
        LoadBalancer lb = new LoadBalancer();
        lb.add(132);
        lb.add(159);
        lb.remove(159);
        lb.add(911);
        System.out.println(lb.pick());
    }
}
