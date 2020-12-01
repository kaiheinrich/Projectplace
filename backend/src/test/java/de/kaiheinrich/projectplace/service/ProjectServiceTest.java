/*
package de.kaiheinrich.projectplace.service;

import de.kaiheinrich.projectplace.db.ProjectMongoDb;
import de.kaiheinrich.projectplace.dto.ProjectDto;
import de.kaiheinrich.projectplace.model.Project;
import de.kaiheinrich.projectplace.utils.IdUtils;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    private final ProjectMongoDb projectDb = mock(ProjectMongoDb.class);
    private final IdUtils idUtils = mock(IdUtils.class);
    private final ProjectService projectService = new ProjectService(projectDb, idUtils);

    @Test
    public void getAllProjectsTest() {

        //Given
        List<Project> projects = List.of(
                new Project("first-id", "kai", "my first project", "my first description"),
                new Project("second-id", "andi324", "andis first project", "andis first description"),
                new Project("third-id", "erich", "erichs first project", "erichs first description")
        );
        when(projectDb.findAll()).thenReturn(projects);

        //When
        List<Project> resultList = projectService.getProjects();

        //Then
        assertThat(resultList, is(projects));
    }

    @Test
    public void getProjectByIdShouldReturnCorrectProject() {

        //Given
        String id = "first-id";
        when(projectDb.findById(id)).thenReturn(Optional.of(
                new Project("first-id", "kai", "my first project", "my first description")
        ));

        //When
        Project resultProject = projectService.getProjectById(id).orElseThrow();

        //Then
        assertThat(resultProject, is(new Project("first-id", "kai", "my first project", "my first description")));
    }

    @Test
    public void getProjectByIdShouldReturnEmptyOptionalWhenIdIsUnknown() {

        //Given
        String id = "random-id";
        when(projectDb.findById(id)).thenReturn(Optional.empty());

        //When
        Optional<Project> result = projectService.getProjectById(id);

        //Then
        assertThat(result, is(Optional.empty()));
    }

    @Test
    public void testAddProjectShouldAddANewProject() {

        //Given
        String id = "some-new-id";
        String username = "kai";
        ProjectDto projectDto = new ProjectDto("some new title", "some new description");
        Project expectedProject = new Project(id, username, "some new title", "some new description");
        when(idUtils.generateId()).thenReturn(id);
        when(projectDb.save(expectedProject)).thenReturn(expectedProject);

        //When
        Project result = projectService.addProject(projectDto, username);

        //Then
        assertThat(result, is(expectedProject));
        verify(projectDb).save(expectedProject);
    }

    @Test
    public void testUpdateProjectShouldUpdateAnExistingProject() {

        //Given
        String id = "some-id";
        String username = "kai";
        ProjectDto projectDto = new ProjectDto("some new title", "some new description");
        Project updatedProject = new Project(id, username,"some new title", "some new description");
        when(projectDb.save(updatedProject)).thenReturn(updatedProject);

        //When
        Project result = projectService.updateProject(id, projectDto, username);

        //Then
        assertThat(result, is(updatedProject));
        verify(projectDb).save(updatedProject);
    }

    @Test
    public void testDeleteProject() {

        //Given
        String id = "some-id";

        //When
        projectService.deleteProject(id);

        //Then
        verify(projectDb).deleteById(id);
    }
}*/
