package de.kaiheinrich.projectplace.security.service;

import de.kaiheinrich.projectplace.db.UserMongoDb;
import de.kaiheinrich.projectplace.model.ProjectplaceUser;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class MongoDbUserDetailsService implements UserDetailsService {

    private final UserMongoDb userDb;

    public MongoDbUserDetailsService(UserMongoDb userDb) {
        this.userDb = userDb;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<ProjectplaceUser> userById = userDb.findById(username);
        if(userById.isEmpty()) {
            throw new UsernameNotFoundException("User not found.");
        }

        return new User(username, userById.get().getPassword(), List.of());
    }
}
