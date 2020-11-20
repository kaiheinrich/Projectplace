package de.kaiheinrich.projectplace.controller;

import de.kaiheinrich.projectplace.dto.ProfileDto;
import de.kaiheinrich.projectplace.model.Profile;
import de.kaiheinrich.projectplace.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public List<Profile> getProfiles() {
        return profileService.getProfiles();
    }

    @PutMapping("{username}")
    public Profile updateProfile(@RequestBody ProfileDto profileDto, Principal principal, @PathVariable String username) {
        if(profileService.getProfileByUsername(username).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if(!Objects.equals(principal.getName(), username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return profileService.updateProfile(profileDto, username);
    }
}
