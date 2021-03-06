package de.kaiheinrich.projectplace.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import de.kaiheinrich.projectplace.db.ProfileMongoDb;
import de.kaiheinrich.projectplace.dto.MessageDto;
import de.kaiheinrich.projectplace.dto.ProfileDto;
import de.kaiheinrich.projectplace.model.Message;
import de.kaiheinrich.projectplace.model.Profile;
import de.kaiheinrich.projectplace.model.Project;
import de.kaiheinrich.projectplace.utils.AmazonS3ClientUtils;
import de.kaiheinrich.projectplace.utils.DateExpirationUtils;
import de.kaiheinrich.projectplace.utils.IdUtils;
import de.kaiheinrich.projectplace.utils.TimestampUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileMongoDb profileDb;
    private final AmazonS3 s3Client;
    private final AmazonS3ClientUtils s3ClientUtils;
    private final IdUtils idUtils;
    private final TimestampUtils timestampUtils;
    private final DateExpirationUtils expirationUtils;

    @Autowired
    public ProfileService(ProfileMongoDb profileDb,
                          AmazonS3 s3Client,
                          AmazonS3ClientUtils s3ClientUtils,
                          IdUtils idUtils,
                          TimestampUtils timestampUtils,
                          DateExpirationUtils expirationUtils) {
        this.profileDb = profileDb;
        this.s3Client = s3Client;
        this.s3ClientUtils = s3ClientUtils;
        this.idUtils = idUtils;
        this.timestampUtils = timestampUtils;
        this.expirationUtils = expirationUtils;
    }

    public List<Profile> getProfiles() {
        List<Profile> profileList = profileDb.findAll();
        Date expiration = expirationUtils.getExpirationTime();

        for(Profile profile : profileList) {
            if(!profile.getImageName().equals("")){
                profile.setImageUrl(s3Client.generatePresignedUrl(
                        s3ClientUtils.getBucketName(),
                        profile.getImageName(),
                        expiration,
                        HttpMethod.GET)
                        .toString());
            }
        }
        return profileList;
    }

    public Optional<Profile> getProfileByUsername(String username) {
        return profileDb.findById(username);
    }

    public Profile updateProfile(ProfileDto profileDto, String username) {

        Profile profile = getProfileByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        List<Project> userProjects = profile.getProjects();
        List<Message> receivedMessages = profile.getReceivedMessages();
        List<Message> sentMessages = profile.getSentMessages();

        Profile updatedProfile = Profile.builder()
                .username(username)
                .birthday(profileDto.getBirthday())
                .location(profileDto.getLocation())
                .skills(profileDto.getSkills())
                .name(profileDto.getName())
                .imageName(profileDto.getImageName())
                .projects(userProjects)
                .receivedMessages(receivedMessages)
                .sentMessages(sentMessages)
                .build();

        return profileDb.save(updatedProfile);
    }

    public Message sendMessage(String sender, String recipient, MessageDto messageDto) {

        Profile senderProfile = getProfileByUsername(sender).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Profile recipientProfile = getProfileByUsername(recipient).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Message message = Message.builder()
                .id(idUtils.generateId())
                .timestamp(timestampUtils.generateTimestampEpochSeconds())
                .subject(messageDto.getSubject())
                .message(messageDto.getMessage())
                .sender(sender)
                .recipient(recipient)
                .build();

        senderProfile.getSentMessages().add(message);
        recipientProfile.getReceivedMessages().add(message);
        profileDb.saveAll(List.of(senderProfile, recipientProfile));

        return message;
    }
}
