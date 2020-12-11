package de.kaiheinrich.projectplace.controller;

import de.kaiheinrich.projectplace.dto.ProjectDto;
import de.kaiheinrich.projectplace.model.Project;
import de.kaiheinrich.projectplace.service.ImageUploadAWSService;
import de.kaiheinrich.projectplace.service.ProjectService;
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
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final ImageUploadAWSService imageUploadAWSService;

    @Autowired
    public ProjectController(ProjectService projectService, ImageUploadAWSService imageUploadAWSService) {
        this.projectService = projectService;
        this.imageUploadAWSService = imageUploadAWSService;
    }

    @GetMapping
    public List<Project> getProjects() {
        return projectService.getProjects();
    }

    @PostMapping
    public Project addProject(@RequestBody ProjectDto projectDto, Principal principal) {
        return projectService.addProject(projectDto, principal.getName());
    }

    @PutMapping("{id}")
    public Project updateProject(@RequestBody ProjectDto projectDto, Principal principal, @PathVariable String id) {
        if(projectService.getProjectById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if(!Objects.equals(projectService.getProjectById(id).get().getProjectOwner(), principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return projectService.updateProject(id, projectDto, principal.getName());
    }

    @PostMapping("/image")
    public String uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        return imageUploadAWSService.upload(file);
    }

    @DeleteMapping("{id}")
    public void deleteProject(@PathVariable String id, Principal principal) {
        if(projectService.getProjectById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if(!Objects.equals(projectService.getProjectById(id).get().getProjectOwner(), principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        projectService.deleteProject(id);
    }
}
