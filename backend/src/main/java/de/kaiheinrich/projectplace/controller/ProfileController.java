package de.kaiheinrich.projectplace.controller;

import de.kaiheinrich.projectplace.dto.MessageDto;
import de.kaiheinrich.projectplace.dto.ProfileDto;
import de.kaiheinrich.projectplace.model.Message;
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
        if(!Objects.equals(principal.getName(), username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return profileService.updateProfile(profileDto, username);
    }

    @PostMapping("/image")
    public String uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        return imageUploadAWSService.upload(file);
    }

    @PostMapping("/message/{username}")
    public Message sendMessage(@PathVariable String username, @RequestBody MessageDto messageDto, Principal principal) {
        if(!Objects.equals(principal.getName(), messageDto.getSender())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return profileService.sendMessage(principal.getName(), username, messageDto);
    }
}
