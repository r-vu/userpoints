package fetchrewards.userpoints.models;


import java.time.*;

public class Transaction {
    public static final ZoneId UTC_TIMEZONE = ZoneOffset.UTC;
    private static long NEXT_ID = 1;

    private final long transactionId;
    private final String payer;
    private final User recipient;
    private final int initialPoints;
    private int currentPoints;
    private final LocalDateTime transactionDate;

    public Transaction(String payer, User recipient, int points) {
        this(payer, recipient, points, LocalDateTime.now(UTC_TIMEZONE));
    }

    public Transaction(String payer, User recipient, int points, LocalDateTime time) {
        this.transactionId = NEXT_ID++;
        this.payer = payer;
        this.recipient = recipient;
        this.initialPoints = points;
        this.currentPoints = points;
        this.transactionDate = time;
    }

    public long getTransactionId() {
        return this.transactionId;
    }

    public String getPayer() {
        return this.payer;
    }

    public int getInitialPoints() {
        return this.initialPoints;
    }

    public int getCurrentPoints() {
        return this.currentPoints;
    }

    public boolean equalPayers(Transaction t) {
        return this.payer.equals(t.payer);
    }

    /**
     * Method to deduct points from this transaction and return a value
     * to allow chaining deductions if required
     *
     * @param points Amount to deduct
     * @return Returns the remainder needed to complete a deduction
     */
    public int deductPoints(int points) {
        if (points < 0) {
            return 0;
        }

        if (currentPoints >= points) {
            currentPoints -= points;
            return 0;
        } else {
            int remainder = points - currentPoints;
            currentPoints = 0;
            return remainder;
        }

    }
}
