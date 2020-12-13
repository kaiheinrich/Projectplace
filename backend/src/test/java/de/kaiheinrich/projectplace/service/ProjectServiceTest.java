
package de.kaiheinrich.projectplace.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import de.kaiheinrich.projectplace.db.ProfileMongoDb;
import de.kaiheinrich.projectplace.db.ProjectMongoDb;
import de.kaiheinrich.projectplace.dto.ProjectDto;
import de.kaiheinrich.projectplace.model.Message;
import de.kaiheinrich.projectplace.model.Profile;
import de.kaiheinrich.projectplace.model.Project;
import de.kaiheinrich.projectplace.utils.AmazonS3ClientUtils;
import de.kaiheinrich.projectplace.utils.DateExpirationUtils;
import de.kaiheinrich.projectplace.utils.IdUtils;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    private final ProjectMongoDb projectDb = mock(ProjectMongoDb.class);
    private final IdUtils idUtils = mock(IdUtils.class);
    private final ProfileMongoDb profileDb = mock(ProfileMongoDb.class);
    private final AmazonS3 s3Client = mock(AmazonS3.class);
    private final AmazonS3ClientUtils s3ClientUtils = mock(AmazonS3ClientUtils.class);
    private final DateExpirationUtils expirationUtils = mock(DateExpirationUtils.class);
    private final ProjectService projectService = new ProjectService(
            projectDb,
            idUtils,
            profileDb,
            s3Client,
            s3ClientUtils,
            expirationUtils);

    @Test
    public void getAllProjectsTestShouldReturnAllProjects() throws MalformedURLException {

        //Given
        List<Project> projects = List.of(
                new Project("first-id", "kai", "my first project", "my first description", "http://www.url1.de", "imagename1", "teaser1"),
                new Project("second-id", "andi324", "andis first project", "andis first description", "http://www.url2.de", "imagename2", "teaser2"),
                new Project("third-id", "erich", "erichs first project", "erichs first description", "http://www.url3.de", "imagename3", "teaser3")
        );
        Date expiration = new Date(2030, Calendar.JANUARY,1);

        when(projectDb.findAll()).thenReturn(projects);
        when(expirationUtils.getExpirationTime()).thenReturn(expiration);
        when(s3ClientUtils.getBucketName()).thenReturn("test-bucket");
        when(s3Client.generatePresignedUrl("test-bucket", "imagename1", expiration, HttpMethod.GET)).thenReturn(new URL("http://www.url1.de"));
        when(s3Client.generatePresignedUrl("test-bucket", "imagename2", expiration, HttpMethod.GET)).thenReturn(new URL("http://www.url2.de"));
        when(s3Client.generatePresignedUrl("test-bucket", "imagename3", expiration, HttpMethod.GET)).thenReturn(new URL("http://www.url3.de"));


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
                new Project("first-id", "kai", "my first project", "my first description", null, "imagename1", "teaser1")
        ));

        //When
        Project resultProject = projectService.getProjectById(id).orElseThrow();

        //Then
        assertThat(resultProject, is(new Project("first-id", "kai", "my first project", "my first description", null, "imagename1", "teaser1")));
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
        String username = "andi324";
        Profile profile = new Profile("andi324","Andreas", LocalDate.of(1900, 10, 24),
                "Berlin", new ArrayList<>(List.of("Cars and couches")), "http://www.url1.de", "imageName1",
                new ArrayList<>(List.of(new Project("projectid1", "andi324", "title1", "description1", "projectimageurl1", "projectimage1", "teaser1"))),
                new ArrayList<>(List.of(new Message("messageid1", Instant.parse("2020-10-24T14:15:00Z"), "subject1", "message1", "birgit116", "andi324"))),
                new ArrayList<>(List.of(new Message("messageid2", Instant.parse("2020-10-24T14:15:00Z"), "subject2", "message2", "andi324", "birgit116"))));
        Profile profileWithNewProject = new Profile("andi324","Andreas", LocalDate.of(1900, 10, 24),
                "Berlin", new ArrayList<>(List.of("Cars and couches")), "http://www.url1.de", "imageName1",
                new ArrayList<>(List.of(
                        new Project("projectid1", "andi324", "title1", "description1", "projectimageurl1", "projectimage1", "teaser1"),
                        new Project(id, username, "some new title", "some new description", "", "someimage", "someteaser"))),
                new ArrayList<>(List.of(new Message("messageid1", Instant.parse("2020-10-24T14:15:00Z"), "subject1", "message1", "birgit116", "andi324"))),
                new ArrayList<>(List.of(new Message("messageid2", Instant.parse("2020-10-24T14:15:00Z"), "subject2", "message2", "andi324", "birgit116"))));
        ProjectDto projectDto = new ProjectDto("some new title", "some new description", "someimage", "someteaser");
        Project expectedProject = new Project(id, username, "some new title", "some new description", "", "someimage", "someteaser");



        when(idUtils.generateId()).thenReturn(id);
        when(profileDb.findById(username)).thenReturn(Optional.of(profile));
        when(projectDb.save(expectedProject)).thenReturn(expectedProject);

        //When
        Project result = projectService.addProject(projectDto, username);

        //Then
        assertThat(result, is(expectedProject));
        verify(projectDb).save(expectedProject);
        verify(profileDb).save(profileWithNewProject);
    }

    @Test
    public void testUpdateProjectShouldUpdateAnExistingProject() {

        //Given
        String id = "some-id";
        String username = "kai";
        ProjectDto projectDto = new ProjectDto("some new title", "some new description", "someimage", "someteaser");
        Project updatedProject = new Project(id, username, "some new title", "some new description", null, "someimage", "someteaser");
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
        String username = "andi324";
        Profile profile = new Profile("andi324","Andreas", LocalDate.of(1900, 10, 24),
                "Berlin", new ArrayList<>(List.of("Cars and couches")), "http://www.url1.de", "imageName1",
                new ArrayList<>(List.of(
                        new Project("projectid1", "andi324", "title1", "description1", "projectimageurl1", "projectimage1", "teaser1"),
                        new Project(id, username, "some new title", "some new description", "", "someimage", "someteaser"))),
                new ArrayList<>(List.of(new Message("messageid1", Instant.parse("2020-10-24T14:15:00Z"), "subject1", "message1", "birgit116", "andi324"))),
                new ArrayList<>(List.of(new Message("messageid2", Instant.parse("2020-10-24T14:15:00Z"), "subject2", "message2", "andi324", "birgit116"))));
        Profile profileWithoutDeletedProject = new Profile("andi324","Andreas", LocalDate.of(1900, 10, 24),
                "Berlin", new ArrayList<>(List.of("Cars and couches")), "http://www.url1.de", "imageName1",
                new ArrayList<>(List.of(new Project("projectid1", "andi324", "title1", "description1", "projectimageurl1", "projectimage1", "teaser1"))),
                new ArrayList<>(List.of(new Message("messageid1", Instant.parse("2020-10-24T14:15:00Z"), "subject1", "message1", "birgit116", "andi324"))),
                new ArrayList<>(List.of(new Message("messageid2", Instant.parse("2020-10-24T14:15:00Z"), "subject2", "message2", "andi324", "birgit116"))));
        Project projectToDelete = new Project(id, username, "some new title", "some new description", "", "someimage", "someteaser");

        when(projectDb.findById(id)).thenReturn(Optional.of(projectToDelete));
        when(profileDb.findById(username)).thenReturn(Optional.of(profile));

        //When
        projectService.deleteProject(id, username);

        //Then
        verify(projectDb).deleteById(id);
        verify(profileDb).save(profileWithoutDeletedProject);
    }
}