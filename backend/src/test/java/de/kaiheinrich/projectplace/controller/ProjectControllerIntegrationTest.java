package de.kaiheinrich.projectplace.controller;

import de.kaiheinrich.projectplace.db.ProjectMongoDb;
import de.kaiheinrich.projectplace.db.UserMongoDb;
import de.kaiheinrich.projectplace.dto.ProjectDto;
import de.kaiheinrich.projectplace.model.Project;
import de.kaiheinrich.projectplace.model.ProjectplaceUser;
import de.kaiheinrich.projectplace.security.dto.LoginDto;
import de.kaiheinrich.projectplace.utils.IdUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "jwt.secretkey=someverycoolsecretkey")
class ProjectControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProjectMongoDb projectDb;

    @Autowired
    private UserMongoDb userDb;

    @MockBean
    private IdUtils idUtils;

    @BeforeEach
    public void setup() {

        projectDb.deleteAll();
        projectDb.saveAll(List.of(
                new Project("id1", "kai", "title1", "description1"),
                new Project("id2", "andi324", "title2", "description2"),
                new Project("id3", "monika", "title3", "description3")
        ));

        userDb.deleteAll();
        String password = "password";
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        userDb.save(new ProjectplaceUser("kai", encodedPassword));
    }

    private String getProjectsUrl() {
        return "http://localhost:" + port + "/api/projects";
    }

    private String login() {

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/auth/login", new LoginDto(
                "kai",
                "password"
        ), String.class);

        return response.getBody();
    }

    private <T> HttpEntity<T> getValidAuthorizationEntity(T data) {

        String token = login();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<T>(data,headers);
    }

    @Test
    public void testGetMappingShouldReturnAllProjects() {

        //Given
        List<Project> projectList = List.of(
                new Project("id1", "kai", "title1", "description1"),
                new Project("id2", "andi324", "title2", "description2"),
                new Project("id3", "monika", "title3", "description3")
        );

        //When
        HttpEntity<Void> entity = getValidAuthorizationEntity(null);
        ResponseEntity<Project[]> response = restTemplate.exchange(getProjectsUrl(), HttpMethod.GET, entity, Project[].class);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(projectList.toArray()));
    }

    @Test
    public void testGetMappingShouldReturnForbiddenWithoutToken() {

        //When
        ResponseEntity<Void> response = restTemplate.getForEntity(getProjectsUrl(), Void.class);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void testAddProjectShouldAddNewProjectToDb() {

        //Given
        ProjectDto projectDto = new ProjectDto("some title", "some description");
        Project expected = new Project("some-id", "kai", "some title", "some description");
        when(idUtils.generateId()).thenReturn("some-id");

        //When
        HttpEntity<ProjectDto> entity = getValidAuthorizationEntity(projectDto);
        ResponseEntity<Project> response = restTemplate.exchange(getProjectsUrl(), HttpMethod.POST, entity, Project.class);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(expected));
    }

    @Test
    public void testUpdateProjectShouldUpdateExistingProjectOfCorrectUser() {

        //Given
        String id = "id1";
        String url = getProjectsUrl() + "/" + id;
        Project expectedProject = new Project("id1", "kai", "new title", "new description");
        ProjectDto projectDto = new ProjectDto("new title", "new description");

        //When
        HttpEntity<ProjectDto> entity = getValidAuthorizationEntity(projectDto);
        ResponseEntity<Project> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Project.class);
        Optional<Project> savedProject = projectDb.findById(id);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(expectedProject));
        assertThat(savedProject.get(), is(expectedProject));
    }

    @Test
    public void testUpdateProjectShouldThrow404WhenIdNotFound() {

        //Given
        String id = "some-unknown-id";
        String url = getProjectsUrl() + "/" + id;
        ProjectDto projectDto = new ProjectDto("new title", "new description");

        //When
        HttpEntity<ProjectDto> entity = getValidAuthorizationEntity(projectDto);
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void testUpdateProjectShouldReturn403WhenCurrentUserAndProjectOwnerDoNotMatch() {

        //Given
        String id = "id2";
        String url = getProjectsUrl() + "/" + id;
        ProjectDto projectDto = new ProjectDto("new title", "new description");

        //When
        HttpEntity<ProjectDto> entity = getValidAuthorizationEntity(projectDto);
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void testDeleteProjectShouldDeleteProjectFromDb() {

        //Given
        String id = "id1";
        String url = getProjectsUrl() + "/" + id;

        //When
        HttpEntity<Void> entity = getValidAuthorizationEntity(null);
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
        Optional<Project> result = projectDb.findById(id);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(result, is(Optional.empty()));
    }

    @Test
    public void testDeleteProjectShouldThrow404WhenIdNotFound() {

        //Given
        String id = "some-unknown-id";
        String url = getProjectsUrl() + "/" + id;

        //When
        HttpEntity<Void> entity = getValidAuthorizationEntity(null);
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void testDeleteProjectShouldReturn403WhenCurrentUserAndProjectOwnerDoNotMatch() {

        //Given
        String id = "id2";
        String url = getProjectsUrl() + "/" + id;

        //When
        HttpEntity<Void> entity = getValidAuthorizationEntity(null);
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }
}