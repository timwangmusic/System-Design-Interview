import java.util.HashMap;
import java.util.HashSet;

class ListNode {
    int value;
    ListNode prev;
    ListNode next;

    public ListNode(int val) {
        value = val;
        prev = null;
        next = null;
    }
}

class FirstUnique {
    HashMap<Integer, ListNode> uniques;
    HashSet<Integer> duplicates;
    ListNode head;
    ListNode tail;

    public FirstUnique(int[] nums) {
        uniques = new HashMap<>();
        duplicates = new HashSet<>();
        head = new ListNode(0);
        tail = new ListNode(0);
        head.next = tail;
        tail.prev = head;
        for (int num : nums) {
            add(num);
        }
    }

    public int showFirstUnique() {
        if (uniques.size() == 0) {
            return -1;
        }
        return head.next.value;
    }

    public void add(int value) {
        if (duplicates.contains(value)) {
            return;
        }
        if (uniques.containsKey(value)) {
            duplicates.add(value);
            ListNode n = uniques.get(value);
            ListNode prev = n.prev;
            prev.next = n.next;
            if (n.next != null) {
                n.next.prev = prev;
            }
            n.next = null;
            n.prev = null;
            uniques.remove(value);
        } else {
            ListNode prev = tail.prev;
            ListNode newNode = new ListNode(value);

            prev.next = newNode;
            newNode.prev = prev;

            newNode.next = tail;
            tail.prev = newNode;

            uniques.put(value, newNode);
        }
    }

    public static void main(String[] args) {
        int[] nums = {2};
        FirstUnique app = new FirstUnique(nums);
        app.add(2);
        app.add(2);
        app.add(2);
        System.out.println(app.showFirstUnique());
    }
}
