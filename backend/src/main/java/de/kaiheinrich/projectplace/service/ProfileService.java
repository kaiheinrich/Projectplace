package de.kaiheinrich.projectplace.service;

import de.kaiheinrich.projectplace.db.ProfileMongoDb;
import de.kaiheinrich.projectplace.dto.ProfileDto;
import de.kaiheinrich.projectplace.model.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileMongoDb profileDb;

    @Autowired
    public ProfileService(ProfileMongoDb profileDb) {
        this.profileDb = profileDb;
    }

    public List<Profile> getProfiles() {
        return profileDb.findAll();
    }

    public Optional<Profile> getProfileByUsername(String username) {
        if(!profileDb.existsById(username)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return profileDb.findById(username);
    }

    public Profile updateProfile(ProfileDto profileDto, String username) {
        Profile updatedProfile = Profile.builder()
                .username(username)
                .age(profileDto.getAge())
                .location(profileDto.getLocation())
                .skills(profileDto.getSkills())
                .name(profileDto.getName())
                .build();

        return profileDb.save(updatedProfile);
    }
}
