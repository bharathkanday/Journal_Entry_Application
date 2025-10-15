package com.kbk.JournalApp.repository;

import com.kbk.JournalApp.entity.JournalEntry;
import com.kbk.JournalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository extends MongoRepository<User, ObjectId> {
//    User findByUserName( String username);
    User findByUsername(String username);

    void deleteByUsername(String username);

}
