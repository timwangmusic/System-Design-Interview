import java.util.*;

public class MiniCassandra {
    HashMap<String, TreeMap<Integer, Column>> cassandra;

    public MiniCassandra() {
        cassandra = new HashMap<>();
    }

    /*
     * @param raw_key: a string
     * @param column_key: An integer
     * @param column_value: a string
     * @return: nothing
     */
    public void insert(String row_key, int column_key, String value) {
        if (!cassandra.containsKey(row_key)) {
            cassandra.put(row_key, new TreeMap<>());
        }
        cassandra.get(row_key).put(column_key, new Column(column_key, value));
    }

    /*
     * return columns in the specified range INCLUSIVE of the end
     * @param row_key: a string
     * @param column_start: An integer
     * @param column_end: An integer
     * @return: a list of Columns
     */
    public List<Column> query(String row_key, int column_start, int column_end) {
        if (!cassandra.containsKey(row_key)) {
            cassandra.put(row_key, new TreeMap<>());
        }
        SortedMap<Integer, Column> m = cassandra.get(row_key).subMap(column_start, column_end+1);

        Iterator iterator = m.keySet().iterator();

        ArrayList<Column> res = new ArrayList<>();

        while (iterator.hasNext()) {
            Integer key = (Integer) iterator.next();
            Column column = m.get(key);
            res.add(column);
        }
        return res;
    }

    public static void main(String[] args) {
        MiniCassandra db = new MiniCassandra();
        db.insert("google", 1, "search");
        db.insert("google", 10, "maps");
        System.out.println(db.query("google", 0, 10));
    }
}
