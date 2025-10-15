package com.kbk.JournalApp.Controller;

import com.kbk.JournalApp.entity.JournalEntry;
import com.kbk.JournalApp.entity.User;
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
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerv2 {

    @Autowired
    private JournalEntryService journalEntryService;


    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        User user=userService.findByUserName(username);
        List<JournalEntry> all=user.getJournalEntries();
        if(all.size()!=0){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry){
        try {
//            User user=userService.findByUserName(username);

//            myEntry.setDate(LocalDateTime.now());
            Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
            String username=authentication.getName();
            journalEntryService.saveEntry(myEntry,username);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

//    @GetMapping("/id/{myId}")
//    public ResponseEntity<JournalEntry> getEntryById(@PathVariable String myId){
//        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
//        String username=authentication.getName();
//        User user=userService.findByUserName(username);
//        List<JournalEntry> collect= user.getJournalEntries().stream().filter(x->x.getId().equals(myId)).collect(Collectors.toList());
//        if(!collect.isEmpty()){
//            ObjectId objectId = new ObjectId(myId);
//            Optional<JournalEntry> journalEntry=journalEntryService.findById(objectId);
//            if(journalEntry.isPresent()){
//                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
//            }
//
//        }
//
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//
//
//    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> getEntryById(@PathVariable String myId){
        ObjectId objectId;
        try {
            objectId = new ObjectId(myId);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // invalid ObjectId
        }

        Optional<JournalEntry> journalEntry = journalEntryService.findById(objectId);

        return journalEntry
                .map(entry -> new ResponseEntity<>(entry, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }



    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteEntryById(@PathVariable ObjectId myId){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        boolean removed=journalEntryService.deleteById(myId,username);
        if(removed){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("id/{id}")
    public ResponseEntity<?> updateEntry(@PathVariable ObjectId id,@RequestBody JournalEntry newEntry){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        User user=userService.findByUserName(username);
        List<JournalEntry> collect=user.getJournalEntries().stream().filter(x->x.getId().equals(id)).collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<JournalEntry> journalEntry=journalEntryService.findById(id);
            if(journalEntry.isPresent()){
                JournalEntry old=journalEntry.get();
                old.setTitle(newEntry.getTitle()!=null && !newEntry.getTitle().isEmpty()?newEntry.getTitle():old.getTitle());
                old.setContent(newEntry.getContent()!=null && !newEntry.getContent().isEmpty()?newEntry.getContent():old.getContent());
                journalEntryService.saveEntry(old);
                return new ResponseEntity<>(old,HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}
