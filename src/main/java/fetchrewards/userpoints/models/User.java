package fetchrewards.userpoints.models;

import java.time.LocalDateTime;
import java.util.*;

public class User {

    private static long NEXT_ID = 1;
    private final long userId;
    private Map<String, Integer> pointBreakdown;
    private int balance;
    private final List<Transaction> transactionList;
    private final Deque<Transaction> transactionDeque;

    public User() {
        this.userId = NEXT_ID++;
        this.pointBreakdown = new HashMap<>();
        this.balance = 0;

        // Used to track all transactions in history
        this.transactionList = new ArrayList<>();

        // Used to serve qualified transactions for deductions
        this.transactionDeque = new ArrayDeque<>();
    }

    public long getUserId() {
        return this.userId;
    }

    public String getPointBreakdown() {
        return null;
    }

    public int getBalance() {
        return this.balance;
    }

    public List<Transaction> getTransactions() {
        return this.transactionList;
    }

    public void addPoints(String payer, int points) {
        Transaction t = new Transaction(payer, this, points);
        addTransaction(t);
    }

    /**
     * Method used to create ineligible transactions, clearing
     * the transactionDeque
     * @return A map of all the transactions created. Empty if operation
     * failed
     */
    public Map<String, Integer> deductPoints(int points) {

        Map<String, Integer> deductionHistory = new HashMap<>();

        if (balance < points) {
            return deductionHistory;
        }


        LocalDateTime timestamp = LocalDateTime.now(Transaction.UTC_TIMEZONE);

        while (points > 0 && !transactionDeque.isEmpty()) {
            Transaction current = transactionDeque.peekFirst();

            Transaction next;
            if (points >= current.getCurrentPoints()) {
                next = new Transaction(
                    current.getPayer(), this, -1 * current.getCurrentPoints(), timestamp);
                addTransaction(next);
                transactionDeque.removeFirst();
                points -= current.getCurrentPoints();
            } else {
                next = new Transaction(
                    current.getPayer(), this, -1 * (current.getCurrentPoints() - points), timestamp);
                addTransaction(next);
                points = 0;
            }

            deductionHistory.put(
                next.getPayer(), deductionHistory.getOrDefault(next.getPayer(), 0) + next.getInitialPoints());
        }

        return deductionHistory;
    }

    private void addTransaction(Transaction t) {
        if (t.getInitialPoints() > 0) {
            transactionDeque.addLast(t);
        } else if (t.getInitialPoints() < 0) {
            for (Transaction transaction : transactionDeque) {
                if (t.equalPayers(transaction)) {
                    int remainder = transaction.deductPoints(-1 * t.getInitialPoints());
                    if (remainder == 0) {
                        break;
                    }
                }
            }
        }

        balance += t.getInitialPoints();
        pointBreakdown.put(t.getPayer(), pointBreakdown.getOrDefault(t.getPayer(), 0) + t.getInitialPoints());
        transactionList.add(t);
    }
}