package fetchrewards.userpoints.services;

import fetchrewards.userpoints.models.Transaction;
import fetchrewards.userpoints.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    // Usually this would be an @Autowired repository or similar
    // However for this challenge, only need to keep in program memory
    private static final Map<Long, User> userMap = new HashMap<>();

    public User findUserById(long id) {
        if (!userMap.containsKey(id)) {
            throw new UserNotFoundException();
        }

        return userMap.get(id);
    }

    public User createUser() {
        User newUser = new User();
        userMap.put(newUser.getUserId(), newUser);
        return newUser;
    }

    public void addPointsToUser(long id, String payer, int points) {
        User current = userMap.get(id);
        current.addPoints(payer, points);
    }

    public Map<String, Integer> deductPointsFromUser(long id, int points) {
        User current = userMap.get(id);
        return current.deductPoints(points);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User not found")
    private static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException() {
            super();
        }
    }

}
