package com.kbk.JournalApp.service;

import com.kbk.JournalApp.entity.User;
import com.kbk.JournalApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

//    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private PasswordEncoder passwordEncoder;

//    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public void saveNewEntry(User user){
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
        }
        catch (Exception e){
            log.info("information log");
            log.error("error occured for {}", user.getUsername());
            log.warn(e.getMessage());
            log.trace(e.getMessage());
            log.debug(e.getMessage());

        }

    }

    public void saveAdmin(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER", "ADMIN"));
        userRepository.save(user);
    }

    public void saveUser(User user){

        userRepository.save(user);
    }

    public List<User> getAll(){

        return userRepository.findAll();
    }

    public Optional<User> findById(ObjectId myId){

        return userRepository.findById(myId);
    }

    public void deleteById(ObjectId myId){
        userRepository.deleteById(myId);

    }

    public User findByUserName(String userName) {
        return userRepository.findByUsername(userName);
    }
}
