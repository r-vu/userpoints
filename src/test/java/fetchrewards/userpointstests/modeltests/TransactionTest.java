package fetchrewards.userpointstests.modeltests;

import fetchrewards.userpoints.models.Transaction;
import fetchrewards.userpoints.models.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    @Test
    public void TestIncrementId() {
        User user1 = new User();
        Transaction transaction1 = new Transaction("PAYER", user1, 100);
        assertEquals(1, transaction1.getTransactionId());
        Transaction transaction2 = new Transaction("PAYER", user1, 200);
        assertEquals(2, transaction2.getTransactionId());
    }

    @Test
    public void DeductionTest_SufficientPoints() {
        User user1 = new User();
        Transaction transaction1 = new Transaction("PAYER", user1, 100);
        int result1 = transaction1.deductPoints(100);
        assertEquals(0, result1);
    }

    @Test
    public void DeductionTest_InsufficientPoints() {
        User user1 = new User();
        Transaction transaction1 = new Transaction("PAYER", user1, 100);
        int result1 = transaction1.deductPoints(101);
        assertEquals(1, result1);
    }
}
