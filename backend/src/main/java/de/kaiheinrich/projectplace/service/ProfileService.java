package de.kaiheinrich.projectplace.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import de.kaiheinrich.projectplace.db.ProfileMongoDb;
import de.kaiheinrich.projectplace.dto.ProfileDto;
import de.kaiheinrich.projectplace.model.Profile;
import de.kaiheinrich.projectplace.model.Project;
import de.kaiheinrich.projectplace.utils.AmazonS3ClientUtils;
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

    @Autowired
    public ProfileService(ProfileMongoDb profileDb, AmazonS3ClientUtils s3ClientUtils) {
        this.profileDb = profileDb;
        this.s3ClientUtils = s3ClientUtils;
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

        Profile updatedProfile = Profile.builder()
                .username(username)
                .birthday(profileDto.getBirthday())
                .location(profileDto.getLocation())
                .skills(profileDto.getSkills())
                .name(profileDto.getName())
                .imageName(profileDto.getImageName())
                .projects(userProjects)
                .build();

        return profileDb.save(updatedProfile);
    }

    private Date getExpirationTime() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);
        return expiration;
    }
}
