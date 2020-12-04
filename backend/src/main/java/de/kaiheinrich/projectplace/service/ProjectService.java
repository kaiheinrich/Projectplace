package de.kaiheinrich.projectplace.service;

import de.kaiheinrich.projectplace.db.ProfileMongoDb;
import de.kaiheinrich.projectplace.db.ProjectMongoDb;
import de.kaiheinrich.projectplace.dto.ProjectDto;
import de.kaiheinrich.projectplace.model.Profile;
import de.kaiheinrich.projectplace.model.Project;
import de.kaiheinrich.projectplace.utils.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectMongoDb projectDb;
    private final IdUtils idUtils;
    private final ProfileMongoDb profileDb;

    @Autowired
    public ProjectService(ProjectMongoDb projectDb, IdUtils idUtils, ProfileMongoDb profileDb) {
        this.projectDb = projectDb;
        this.idUtils = idUtils;
        this.profileDb = profileDb;
    }

    public List<Project> getProjects() {
        return projectDb.findAll();
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
                .build();

        userProfile.getProjects().add(newProject);

        Profile updatedProfile = Profile.builder()
                .username(username)
                .birthday(userProfile.getBirthday())
                .location(userProfile.getLocation())
                .skills(userProfile.getSkills())
                .name(userProfile.getName())
                .imageName(userProfile.getImageName())
                .projects(userProfile.getProjects())
                .build();

        profileDb.save(updatedProfile);

        return projectDb.save(newProject);
    }

    public Project updateProject(String id, ProjectDto projectDto, String username) {
        Project updatedProject = Project.builder()
                .id(id)
                .projectOwner(username)
                .title(projectDto.getTitle())
                .description(projectDto.getDescription())
                .build();
        return projectDb.save(updatedProject);
    }


    public void deleteProject(String id) {
        projectDb.deleteById(id);
    }
}
