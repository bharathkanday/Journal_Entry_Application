package com.kbk.JournalApp.service;

import com.kbk.JournalApp.entity.JournalEntry;
import com.kbk.JournalApp.entity.User;
import com.kbk.JournalApp.repository.JournalEntryRepository;
import com.kbk.JournalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(JournalEntryService.class);

    @Transactional
    public void saveEntry(JournalEntry jEntry, String username){
        try{
            User user=userService.findByUserName(username);
            jEntry.setDate(LocalDateTime.now());
            JournalEntry saved=journalEntryRepository.save(jEntry);
            user.getJournalEntries().add(saved);
//            user.setUsername(null);
            userService.saveUser(user);
        }
        catch(Exception e){
            log.info("information log");
            System.out.println(e);
            throw new RuntimeException("An error occured while saving entry ",e);
        }

    }

    public void saveEntry(JournalEntry jEntry){
//        User user=userService.findByUserName(username);
//        jEntry.setDate(LocalDateTime.now());
//        JournalEntry saved=journalEntryRepository.save(jEntry);
//        user.getJournalEntries().add(saved);
         journalEntryRepository.save(jEntry);
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId myId){
        return journalEntryRepository.findById(myId);
    }

    @Transactional
    public boolean deleteById(ObjectId myId,String username){
        boolean removed=false;
        try {
            User user=userService.findByUserName(username);
            removed =user.getJournalEntries().removeIf(x->x.getId().equals(myId));
            if(removed){
                userService.saveUser(user);
                journalEntryRepository.deleteById(myId);
            }

        }catch(Exception e){
            System.out.println(e);
            throw new RuntimeException("An error occured while deleting entry ",e);
        }
        return removed;

    }

//    public List<JournalEntry> findByUsername(String username){
//
//    }
}
