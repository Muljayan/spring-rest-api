package com.msj.webservices.springrestapi.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class UserResource {

    @Autowired
    private UserDaoService service;

    //    retrieve all users
    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return service.findAll();
    }

    //    retrieve details of one user
    @GetMapping("/users/{id}")
    public User retrieveUser(@PathVariable int id) {
        User user = service.findOne(id);
        if (user == null) {
            throw new UserNotFoundException("id-" + id);
        }
        return user;
    }

    //    Create user
//    input details of user
//    output created and return created uri
    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        User savedUser = service.save(user);
//        Return created status
//        Return the created user
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

}

