package de.kaiheinrich.projectplace.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import de.kaiheinrich.projectplace.db.ProfileMongoDb;
import de.kaiheinrich.projectplace.db.ProjectMongoDb;
import de.kaiheinrich.projectplace.dto.ProjectDto;
import de.kaiheinrich.projectplace.model.Profile;
import de.kaiheinrich.projectplace.model.Project;
import de.kaiheinrich.projectplace.utils.AmazonS3ClientUtils;
import de.kaiheinrich.projectplace.utils.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Value("${aws.bucket.name}")
    private String bucketName;

    private final ProjectMongoDb projectDb;
    private final IdUtils idUtils;
    private final ProfileMongoDb profileDb;
    private final AmazonS3ClientUtils s3ClientUtils;

    @Autowired
    public ProjectService(ProjectMongoDb projectDb, IdUtils idUtils, ProfileMongoDb profileDb, AmazonS3ClientUtils s3ClientUtils) {
        this.projectDb = projectDb;
        this.idUtils = idUtils;
        this.profileDb = profileDb;
        this.s3ClientUtils = s3ClientUtils;
    }

    public List<Project> getProjects() {

        List<Project> projectList = projectDb.findAll();
        Date expiration = getExpirationTime();
        AmazonS3 s3Client = s3ClientUtils.getS3Client();

        for(Project project : projectList) {
            if(!project.getImageName().equals("")) {
                GeneratePresignedUrlRequest generatePresignedUrlRequest =
                        new GeneratePresignedUrlRequest(bucketName, project.getImageName())
                                .withMethod(HttpMethod.GET)
                                .withExpiration(expiration);
                project.setImageUrl(s3Client.generatePresignedUrl(generatePresignedUrlRequest).toString());
            }
        }

        return projectList;
    }

    public Optional<Project> getProjectById(String id) {
        return projectDb.findById(id);
    }

    public Project addProject(ProjectDto projectDto, String username) {

        Profile userProfile = profileDb.findById(username).get();

        Project newProject = Project.builder()
                .id(idUtils.generateId())
                .projectOwner(username)
                .title(projectDto.getTitle())
                .description(projectDto.getDescription())
                .imageName(projectDto.getImageName())
                .imageUrl("")
                .teaser(projectDto.getTeaser())
                .build();

        userProfile.getProjects().add(newProject);
        profileDb.save(userProfile);

        return projectDb.save(newProject);
    }

    public Project updateProject(String id, ProjectDto projectDto, String username) {
        Project updatedProject = Project.builder()
                .id(id)
                .projectOwner(username)
                .title(projectDto.getTitle())
                .description(projectDto.getDescription())
                .imageName(projectDto.getImageName())
                .teaser(projectDto.getTeaser())
                .build();
        return projectDb.save(updatedProject);
    }


    public void deleteProject(String id) {
        projectDb.deleteById(id);
    }

    private Date getExpirationTime() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);
        return expiration;
    }
}
