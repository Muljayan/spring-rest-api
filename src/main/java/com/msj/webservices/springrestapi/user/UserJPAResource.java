package com.msj.webservices.springrestapi.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


@RestController
public class UserJPAResource {

    @Autowired
    private UserRepository userRepository;

    //    retrieve all users
    @GetMapping("/jpa/users")
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    //    retrieve details of one user
    @GetMapping("/jpa/users/{id}")
    public Resource<User> retrieveUser(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent())
            throw new UserNotFoundException("id-" + id);

        //"all-users", SERVER_PATH + "/users"
        //retrieveAllUsers
        Resource<User> resource = new Resource<User>(user.get());

        ControllerLinkBuilder linkTo =
                linkTo(methodOn(this.getClass()).retrieveAllUsers());

        resource.add(linkTo.withRel("all-users"));
//            Resource<User> resource = new Resource<>(user);
//        ControllerLinkBuilder linkTo = link
        //HATEOAS

        return resource;
    }

    //    Create user
    @PostMapping("/jpa/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId()).toUri();

        return ResponseEntity.created(location).build();

    }


    //    delete user
    @DeleteMapping("/jpa/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
    }
}

