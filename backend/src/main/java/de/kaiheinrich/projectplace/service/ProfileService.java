package de.kaiheinrich.projectplace.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import de.kaiheinrich.projectplace.db.ProfileMongoDb;
import de.kaiheinrich.projectplace.dto.ProfileDto;
import de.kaiheinrich.projectplace.model.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    @Value("${aws.access.key}")
    private String accessKey;

    @Value("${aws.secret.key}")
    private String secretKey;

    private final ProfileMongoDb profileDb;


    @Autowired
    public ProfileService(ProfileMongoDb profileDb) {
        this.profileDb = profileDb;
    }

    public List<Profile> getProfiles() {

        Regions clientRegion = Regions.EU_CENTRAL_1;
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        String bucketName = "kais-super-bucket";

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(clientRegion).withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();

        List<Profile> profileList = profileDb.findAll();

        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);

        for(Profile profile : profileList) {

            if(!profile.getImageUrl().equals("")){
                GeneratePresignedUrlRequest generatePresignedUrlRequest =
                        new GeneratePresignedUrlRequest(bucketName, profile.getImageUrl())
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
        Profile updatedProfile = Profile.builder()
                .username(username)
                .birthday(profileDto.getBirthday())
                .location(profileDto.getLocation())
                .skills(profileDto.getSkills())
                .name(profileDto.getName())
                .imageUrl(profileDto.getImageUrl())
                .build();

        return profileDb.save(updatedProfile);
    }
}
