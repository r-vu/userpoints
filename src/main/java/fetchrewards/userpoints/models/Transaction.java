package fetchrewards.userpoints.models;


import java.time.*;

public class Transaction {
    private static long NEXT_ID = 1;
    private static final ZoneId UTC_TIMEZONE = ZoneOffset.UTC;
    private final long transactionId;
    private final String payer;
    private final User recipient;
    private final int initialPoints;
    private int currentPoints;
    private final LocalDateTime transactionDate;

    public Transaction(String payer, User recipient, int points) {
        this.transactionId = NEXT_ID++;
        this.payer = payer;
        this.recipient = recipient;
        this.initialPoints = points;
        this.currentPoints = points;
        this.transactionDate = LocalDateTime.now(UTC_TIMEZONE);
    }
}
