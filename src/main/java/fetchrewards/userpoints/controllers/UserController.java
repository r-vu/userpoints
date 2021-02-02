package fetchrewards.userpoints.controllers;

import fetchrewards.userpoints.models.User;
import fetchrewards.userpoints.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "")
    public HttpEntity<CollectionModel<User>> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping(path = "/{id}")
    public HttpEntity<EntityModel<User>> getUser(@PathVariable long id) {
        return userService.findUserById(id);
    }

    @PostMapping(path = "")
    public HttpEntity<EntityModel<User>> createUser() {
        return userService.createUser();
    }

    @PostMapping(path = "/{id}/addpoints")
    public HttpEntity<EntityModel<User>> addPointsToUser(
        @PathVariable Long id,
        @RequestParam String payer,
        @RequestParam Integer points
    ) {
        return userService.addPointsToUser(id, payer, points);
    }

    @PostMapping(path = "/{id}/deductpoints")
    public HttpEntity<Map<String, Integer>> deductPointsFromUser(
        @PathVariable Long id,
        @RequestParam Integer points
    ) {
        return userService.deductPointsFromUser(id, points);
    }

    @GetMapping(path = "/{id}/pointbreakdown")
    public HttpEntity<Map<String, Integer>> getPointBreakdown(@PathVariable Long id) {
        return userService.pointBreakdown(id);
    }
}
