package fetchrewards.userpoints.services;

import fetchrewards.userpoints.controllers.UserController;
import fetchrewards.userpoints.models.User;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserService {

    // Usually this would be an @Autowired repository or similar
    // However for this challenge, only need to keep in program memory
    private static final Map<Long, User> userMap = new HashMap<>();

    public HttpEntity<CollectionModel<User>> findAll() {
        Link self = linkTo(methodOn(UserController.class)
            .getAllUsers())
            .withSelfRel();

        Link createUser = linkTo(methodOn(UserController.class)
            .createUser())
            .withRel("createUser");

        CollectionModel<User> collectionModel = CollectionModel.of(userMap.values());
        collectionModel.add(self);
        collectionModel.add(createUser);
        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    public HttpEntity<EntityModel<User>> findUserById(long id) {
        if (!userMap.containsKey(id)) {
            throw new UserNotFoundException();
        }

        return new ResponseEntity<>(addLinks(userMap.get(id)), HttpStatus.OK);
    }

    public HttpEntity<EntityModel<User>> createUser() {
        User newUser = new User();
        userMap.put(newUser.getUserId(), newUser);

        URI location = linkTo(methodOn(UserController.class).getUser(newUser.getUserId())).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    public HttpEntity<EntityModel<User>> addPointsToUser(long id, String payer, int points) {

        User current = userMap.get(id);
        current.addPoints(payer, points);
        return new ResponseEntity<>(EntityModel.of(current), HttpStatus.OK);
    }

    public HttpEntity<Map<String, Integer>> deductPointsFromUser(long id, int points) {
        User current = userMap.get(id);
        return new ResponseEntity<>(current.deductPoints(points), HttpStatus.OK);
    }

    public HttpEntity<Map<String, Integer>> pointBreakdown(long id) {
        User current = userMap.get(id);
        return new ResponseEntity<>(current.getPointBreakdown(), HttpStatus.OK);
    }

    private static EntityModel<User> addLinks(User user) {
        Link self = linkTo(methodOn(UserController.class)
            .getUser(user.getUserId()))
            .withSelfRel();

        Link addPoints = linkTo(methodOn(UserController.class)
            .addPointsToUser(user.getUserId(), null, null))
            .withRel("addPoints");

        Link deductPoints = linkTo(methodOn(UserController.class)
            .deductPointsFromUser(user.getUserId(), null))
            .withRel("deductPoints");

        Link userList = linkTo(methodOn(UserController.class)
            .getAllUsers())
            .withRel("userList");

        user.add(self);
        user.add(addPoints);
        user.add(deductPoints);
        user.add(userList);
        return EntityModel.of(user);
    }



    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User not found")
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException() {
            super();
        }
    }

}
