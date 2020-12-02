package de.kaiheinrich.projectplace.controller;

import de.kaiheinrich.projectplace.dto.ProfileDto;
import de.kaiheinrich.projectplace.model.Profile;
import de.kaiheinrich.projectplace.service.ImageUploadAWSService;
import de.kaiheinrich.projectplace.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;
    private final ImageUploadAWSService imageUploadAWSService;

    @Autowired
    public ProfileController(ProfileService profileService, ImageUploadAWSService imageUploadAWSService) {
        this.profileService = profileService;
        this.imageUploadAWSService = imageUploadAWSService;
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

    @PostMapping("/image")
    public String uploadImage(@RequestParam("image") MultipartFile file) throws IOException, InterruptedException {
        return imageUploadAWSService.upload(file);
    }
}
