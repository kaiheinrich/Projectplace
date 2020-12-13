package de.kaiheinrich.projectplace.service;

import de.kaiheinrich.projectplace.db.ProfileMongoDb;
import de.kaiheinrich.projectplace.db.UserMongoDb;
import de.kaiheinrich.projectplace.dto.SignUpDto;
import de.kaiheinrich.projectplace.model.Profile;
import de.kaiheinrich.projectplace.model.ProjectplaceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SignUpService {

    private final UserMongoDb userDb;
    private final ProfileMongoDb profileDb;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public SignUpService(UserMongoDb userDb, ProfileMongoDb profileDb) {
        this.userDb = userDb;
        this.profileDb = profileDb;
    }

    public Optional<String> signUp(SignUpDto signUpDto) {

        Optional<ProjectplaceUser> user = userDb.findById(signUpDto.getUsername());

        if(user.isPresent()) {
            return Optional.empty();
        }

        String hashedPassword = passwordEncoder.encode(signUpDto.getPassword());
        ProjectplaceUser newUser = new ProjectplaceUser(signUpDto.getUsername(), hashedPassword);
        profileDb.save(new Profile(
                signUpDto.getUsername(),
                signUpDto.getName(),
                signUpDto.getBirthday(),
                "",
                List.of(),
                "",
                "",
                List.of(),
                List.of(),
                List.of()));

        return Optional.of(userDb.save(newUser).getUsername());
    }
}
