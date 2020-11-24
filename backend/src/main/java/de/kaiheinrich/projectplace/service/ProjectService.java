package de.kaiheinrich.projectplace.service;

import de.kaiheinrich.projectplace.db.ProjectMongoDb;
import de.kaiheinrich.projectplace.dto.ProjectDto;
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

    @Autowired
    public ProjectService(ProjectMongoDb projectDb, IdUtils idUtils) {
        this.projectDb = projectDb;
        this.idUtils = idUtils;
    }

    public List<Project> getProjects() {
        return projectDb.findAll();
    }

    public Optional<Project> getProjectById(String id) {
        return projectDb.findById(id);
    }

    public Project addProject(ProjectDto projectDto, String username) {
        Project newProject = Project.builder()
                .id(idUtils.generateId())
                .projectOwner(username)
                .title(projectDto.getTitle())
                .description(projectDto.getDescription())
                .build();
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
