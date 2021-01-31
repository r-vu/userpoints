package fetchrewards.userpointstests.modeltests;

import fetchrewards.userpoints.models.Transaction;
import fetchrewards.userpoints.models.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    @Test
    public void testIncrementId() {
        User user1 = new User();
        Transaction transaction1 = new Transaction("PAYER", user1, 100);
        Transaction transaction2 = new Transaction("PAYER", user1, 200);
        assertEquals(transaction1.getTransactionId() + 1, transaction2.getTransactionId());
    }

    @Test
    public void testDeductPointsSufficientPoints() {
        User user1 = new User();
        Transaction transaction1 = new Transaction("PAYER", user1, 100);
        int result1 = transaction1.deductPoints(100);
        assertEquals(0, result1);
    }

    @Test
    public void testDeductPointsInsufficientPoints() {
        User user1 = new User();
        Transaction transaction1 = new Transaction("PAYER", user1, 100);
        int result1 = transaction1.deductPoints(101);
        assertEquals(1, result1);
    }

    @Test
    public void testEqualPayers() {
        User user1 = new User();
        Transaction transaction1 = new Transaction("PAYER1", user1, 100);
        Transaction transaction2 = new Transaction("PAYER1", user1, 200);
        Transaction transaction3 = new Transaction("PAYER2", user1, 300);
        assertTrue(transaction1.equalPayers(transaction2));
        assertFalse(transaction2.equalPayers(transaction3));
    }
}
