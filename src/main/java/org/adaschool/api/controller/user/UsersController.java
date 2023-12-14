package org.adaschool.api.controller.user;

import org.adaschool.api.exception.UserNotFoundException;
import org.adaschool.api.repository.user.User;
import org.adaschool.api.repository.user.UserDto;
import org.adaschool.api.service.user.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/users/")
public class UsersController {

    private final UsersService usersService;

    public UsersController(@Autowired UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping

    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {

        User newUser = usersService.save(new User(userDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);

    }

    /*   @GetMapping
        public ResponseEntity<List<User>> getAllUsers() {
            //TODO implement this method

            return ResponseEntity.ok(null);
        }*/
    @GetMapping
    public ArrayList <User>getAllUsers()
    {
        return (ArrayList<User>) usersService.all();
    }

    @GetMapping("{id}")
    public ResponseEntity<User> findById(@PathVariable("id") String id) {
        Optional<User> userOptional = usersService.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return ResponseEntity.ok(user);
        } else {
            throw new UserNotFoundException(id);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable("id") String id, @RequestBody UserDto userDto) {
        Optional<User> optionalUser = usersService.findById(id);

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setName(userDto.getName());
            existingUser.setLastName(userDto.getLastName());

            usersService.save(existingUser);

            return ResponseEntity.ok().build();
        }
        else {
            throw new UserNotFoundException(id);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable ("id") String id) {
        Optional<User> existingUserOptional = usersService.findById(id);

        if (existingUserOptional.isPresent()) {
            usersService.deleteById(id);
            return ResponseEntity.ok().build(); // Devuelve una respuesta HTTP 200 OK si la eliminación es exitosa
        } else {
            throw new UserNotFoundException(id); // Devuelve una respuesta HTTP 404 Not Found si el usuario no se encuentra
        }

    }
}