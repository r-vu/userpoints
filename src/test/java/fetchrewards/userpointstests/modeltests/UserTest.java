package fetchrewards.userpointstests.modeltests;

import fetchrewards.userpoints.models.Transaction;
import fetchrewards.userpoints.models.User;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {

    @Test
    public void testIncrementId() {
        User user1 = new User();
        User user2 = new User();
        assertEquals(user1.getUserId() + 1, user2.getUserId());
    }

    @Test
    public void testAddPointsPositiveInput() {
        User user1 = new User();
        user1.addPoints("PAYER", 100);
        assertEquals(100, user1.getBalance());
        assertEquals(1, user1.getTransactions().size());

        user1.addPoints("PAYER", 100);
        assertEquals(200, user1.getBalance());
        assertEquals(2, user1.getTransactions().size());
    }

    /**
     * Currently it is not well-defined if a payer can "refund"
     * what they have put into the account. For now, the behavior is
     * that refunds are prohibited. Any purchasing using points should
     * be done through deductPoints()
     */
    @Test
    public void testAddPointsNegativeInput() {
        User user1 = new User();
        user1.addPoints("PAYER", -100);
        assertEquals(0, user1.getBalance());
        assertEquals(0, user1.getTransactions().size());

        user1.addPoints("PAYER", -1000);
        assertEquals(0, user1.getBalance());
        assertEquals(0, user1.getTransactions().size());
    }

    /**
     * It is not well-defined how a transaction of zero points should
     * be handled. For now, any such transaction is ignored completely
     * and not included in transaction history
     */
    @Test
    public void testAddPointsZero() {
        User user1 = new User();
        user1.addPoints("PAYER", 0);
        assertEquals(0, user1.getBalance());
        assertEquals(0, user1.getTransactions().size());

        user1.addPoints("PAYER", 1);
        assertEquals(1, user1.getBalance());
        assertEquals(1, user1.getTransactions().size());

        user1.addPoints("PAYER", 0);
        assertEquals(1, user1.getBalance());
        assertEquals(1, user1.getTransactions().size());
    }

    @Test
    public void testDeductPointsOnePayer() {
        User user1 = new User();
        user1.addPoints("PAYER", 5000);
        assertEquals(1, user1.getTransactions().size());

        Map<String, Integer> result = user1.deductPoints(3000);

        assertEquals(2000, user1.getBalance());
        assertEquals(1, result.size());
        assertEquals(-3000, result.get("PAYER"));
        assertEquals(2, user1.getTransactions().size());
    }

    @Test
    public void testDeductPointsMultiplePayers() {
        User user1 = new User();
        user1.addPoints("PAYER1", 1);
        user1.addPoints("PAYER2", 2);
        user1.addPoints("PAYER3", 3);
        user1.addPoints("PAYER4", 4);
        user1.addPoints("PAYER5", 5);
        user1.addPoints("PAYER6", 6);

        assertEquals(21, user1.getBalance());
        assertEquals(6, user1.getTransactions().size());

        Map<String, Integer> result = user1.deductPoints(20);
        assertEquals(1, user1.getBalance());
        assertEquals(6, result.size());
        assertEquals(12, user1.getTransactions().size());
        assertEquals(-1, result.get("PAYER1"));
        assertEquals(-2, result.get("PAYER2"));
        assertEquals(-3, result.get("PAYER3"));
        assertEquals(-4, result.get("PAYER4"));
        assertEquals(-5, result.get("PAYER5"));
        assertEquals(-5, result.get("PAYER6"));

        // Test atomicity
        List<Transaction> transactionList = user1.getTransactions();
        ZonedDateTime timestamp = transactionList.get(6).getTransactionDate();

        for (int idx = 7; idx < transactionList.size(); idx++) {
            Transaction current = transactionList.get(idx);
            assertEquals(timestamp, current.getTransactionDate());
        }
    }

    @Test
    public void testDeductPointsInsufficientPoints() {
        User user1 = new User();
        user1.addPoints("PAYER", 50);
        assertEquals(50, user1.getBalance());
        assertEquals(1, user1.getTransactions().size());
        Map<String, Integer> result = user1.deductPoints(100);
        assertEquals(0, result.size());
        assertEquals(50, user1.getBalance());
        assertEquals(1, user1.getTransactions().size());
    }

    /**
     * Adding points should only be done through addPoints(). Thus
     * any calls to deductPoints() with a negative input will be
     * completely ignored
     */
    @Test
    public void testDeductPointsNegativeInput() {
        User user1 = new User();
        user1.addPoints("PAYER", 50);
        assertEquals(50, user1.getBalance());
        assertEquals(1, user1.getTransactions().size());
        Map<String, Integer> result = user1.deductPoints(-100);
        assertEquals(0, result.size());
        assertEquals(50, user1.getBalance());
        assertEquals(1, user1.getTransactions().size());
    }

    @Test
    public void testGetPointBreakdown() {
        User user1 = new User();
        Map<String, Integer> breakdown = user1.getPointBreakdown();

        assertEquals(0, breakdown.size());
        user1.addPoints("PAYER1", 100);
        assertEquals(1, breakdown.size());
        assertEquals(100, breakdown.get("PAYER1"));

        user1.addPoints("PAYER2", 200);
        user1.addPoints("PAYER3", 300);

        assertEquals(3, breakdown.size());
        assertEquals(100, breakdown.get("PAYER1"));
        assertEquals(200, breakdown.get("PAYER2"));
        assertEquals(300, breakdown.get("PAYER3"));

        user1.deductPoints(150);
        assertEquals(3, breakdown.size());
        assertEquals(0, breakdown.get("PAYER1"));
        assertEquals(150, breakdown.get("PAYER2"));
        assertEquals(300, breakdown.get("PAYER3"));
    }

    @Test
    public void testStringifyBreakdown() {
        User user1 = new User();

        user1.addPoints("PAYER1", 100);
        user1.addPoints("PAYER2", 200);
        user1.addPoints("PAYER3", 300);
        user1.deductPoints(150);

        String breakdownString = user1.stringifyBreakdown();
        assertEquals(
            "PAYER1, 0 points\nPAYER2, 150 points\nPAYER3, 300 points\n",
            breakdownString);
    }

    @Test
    public void testStringifyTransactions() {
        User user1 = new User();

        user1.addPoints("PAYER1", 100);
        user1.deductPoints(50);

        String transactionsString = user1.stringifyTransactions();
        assertTrue(transactionsString.matches("^(\\[PAYER1, 100 points,)(.|\\s)*(\\[PAYER1, -50 points,)(.|\\s)*$"));
    }

}
