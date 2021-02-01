package fetchrewards.userpoints.controllers;

import fetchrewards.userpoints.models.User;
import fetchrewards.userpoints.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "")
    public HttpEntity<List<User>> usersSummary() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public HttpEntity<User> getUser(@PathVariable long id) {
        return new ResponseEntity<>(userService.findUserById(id), HttpStatus.OK);
    }

    @PostMapping(path = "/")
    public HttpEntity<User> createUser() {
        return new ResponseEntity<>(userService.createUser(), HttpStatus.CREATED);
    }

    @PostMapping(path = "/{id}/addpoints")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addPointsToUser(
        @PathVariable long id,
        @RequestParam String payer,
        @RequestParam int points
    ) {
        userService.addPointsToUser(id, payer, points);
    }

    @PostMapping(path = "/{id}/deductpoints")
    public HttpEntity<Map<String, Integer>> deductPointsFromUser(
        @PathVariable long id,
        @RequestParam int points
    ) {
        return new ResponseEntity<>(userService.deductPointsFromUser(id, points), HttpStatus.CREATED);
    }
}
