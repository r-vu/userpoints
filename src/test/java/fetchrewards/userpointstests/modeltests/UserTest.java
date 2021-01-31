package fetchrewards.userpointstests.modeltests;

import fetchrewards.userpoints.models.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void TestIncrementId() {
        User user1 = new User();
        assertEquals(1, user1.getUserId());
        User user2 = new User();
        assertEquals(2, user2.getUserId());
    }

}
