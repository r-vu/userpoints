package fetchrewards.userpoints.models;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class User {

    private static long NEXT_ID = 1;
    private final long userId;
    private Map<String, Integer> points;
    private List<Transaction> transactions;

    public User() {
        this.userId = NEXT_ID++;
        this.points = new HashMap<>();
        this.transactions = new ArrayList<>();
    }
}