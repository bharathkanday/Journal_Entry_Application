package com.kbk.JournalApp.Controller;

import com.kbk.JournalApp.entity.JournalEntry;
import com.kbk.JournalApp.entity.User;
import com.kbk.JournalApp.repository.UserRepository;
import com.kbk.JournalApp.service.JournalEntryService;
import com.kbk.JournalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllusers(){
        return userService.getAll();
    }

//    @PostMapping
//    public void createuser(@RequestBody User user){
//        userService.saveEntry(user);
//    }

    @PutMapping
    public ResponseEntity<?> updateuser(@RequestBody User user){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        User userindb=userService.findByUserName(username);
        if(userindb!=null){
            userindb.setUsername(user.getUsername());
            userindb.setPassword(user.getPassword());
            userService.saveNewEntry(userindb);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @DeleteMapping
    public ResponseEntity<?> deleteuser(@RequestBody User user){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUsername(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }




}
