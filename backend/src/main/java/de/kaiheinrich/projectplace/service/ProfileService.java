package de.kaiheinrich.projectplace.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import de.kaiheinrich.projectplace.db.ProfileMongoDb;
import de.kaiheinrich.projectplace.dto.MessageDto;
import de.kaiheinrich.projectplace.dto.ProfileDto;
import de.kaiheinrich.projectplace.model.Message;
import de.kaiheinrich.projectplace.model.Profile;
import de.kaiheinrich.projectplace.model.Project;
import de.kaiheinrich.projectplace.utils.AmazonS3ClientUtils;
import de.kaiheinrich.projectplace.utils.IdUtils;
import de.kaiheinrich.projectplace.utils.TimestampUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    @Value("${aws.bucket.name}")
    private String bucketName;

    private final ProfileMongoDb profileDb;
    private final AmazonS3ClientUtils s3ClientUtils;
    private final IdUtils idUtils;
    private final TimestampUtils timestampUtils;

    @Autowired
    public ProfileService(ProfileMongoDb profileDb, AmazonS3ClientUtils s3ClientUtils, IdUtils idUtils, TimestampUtils timestampUtils) {
        this.profileDb = profileDb;
        this.s3ClientUtils = s3ClientUtils;
        this.idUtils = idUtils;
        this.timestampUtils = timestampUtils;
    }

    public List<Profile> getProfiles() {
        List<Profile> profileList = profileDb.findAll();
        Date expiration = getExpirationTime();
        AmazonS3 s3Client = s3ClientUtils.getS3Client();

        for(Profile profile : profileList) {
            if(!profile.getImageName().equals("")){
                GeneratePresignedUrlRequest generatePresignedUrlRequest =
                        new GeneratePresignedUrlRequest(bucketName, profile.getImageName())
                                .withMethod(HttpMethod.GET)
                                .withExpiration(expiration);
                profile.setImageUrl(s3Client.generatePresignedUrl(generatePresignedUrlRequest).toString());
            }
        }
        return profileList;
    }

    public Optional<Profile> getProfileByUsername(String username) {
        return profileDb.findById(username);
    }

    public Profile updateProfile(ProfileDto profileDto, String username) {

        List<Project> userProjects = getProfileByUsername(username).get().getProjects();
        List<Message> receivedMessages = getProfileByUsername(username).get().getReceivedMessages();
        List<Message> sentMessages = getProfileByUsername(username).get().getSentMessages();

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

        Profile senderProfile = getProfileByUsername(sender).get();
        Profile recipientProfile = getProfileByUsername(recipient).get();

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

        return message;
    }

    private Date getExpirationTime() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);
        return expiration;
    }
}
