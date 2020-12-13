package de.kaiheinrich.projectplace.controller;

import com.amazonaws.services.s3.AmazonS3;
import de.kaiheinrich.projectplace.db.ProfileMongoDb;
import de.kaiheinrich.projectplace.db.ProjectMongoDb;
import de.kaiheinrich.projectplace.db.UserMongoDb;
import de.kaiheinrich.projectplace.dto.ProjectDto;
import de.kaiheinrich.projectplace.model.Message;
import de.kaiheinrich.projectplace.model.Profile;
import de.kaiheinrich.projectplace.model.Project;
import de.kaiheinrich.projectplace.model.ProjectplaceUser;
import de.kaiheinrich.projectplace.security.dto.LoginDto;
import de.kaiheinrich.projectplace.utils.AmazonS3ClientUtils;
import de.kaiheinrich.projectplace.utils.DateExpirationUtils;
import de.kaiheinrich.projectplace.utils.IdUtils;
import de.kaiheinrich.projectplace.utils.TimestampUtils;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

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
    private ProfileMongoDb profileDb;

    @Autowired
    private UserMongoDb userDb;

    @MockBean
    private IdUtils idUtils;

    @MockBean
    private AmazonS3 s3Client;

    @MockBean
    private AmazonS3ClientUtils s3ClientUtils;

    @MockBean
    private DateExpirationUtils expirationUtils;

    @MockBean
    private TimestampUtils timestampUtils;

    private String bucketName = "testbucketname";
    private Date expiration = new Date(2030, Calendar.JANUARY,1);

    @BeforeEach
    public void setup() {

        projectDb.deleteAll();
        projectDb.saveAll(List.of(
                new Project("id1", "kai", "title1", "description1", "http://www.url.de", "imagename", "teaser1"),
                new Project("id2", "andi324", "title2", "description2", "http://www.url.de", "imagename", "teaser2"),
                new Project("id3", "monika", "title3", "description3", "http://www.url.de", "imagename", "teaser3")
        ));

        profileDb.deleteAll();
        profileDb.saveAll(List.of(
                new Profile("andi324","Andreas", LocalDate.of(1900, 10, 24),
                        "Berlin", new ArrayList<>(List.of("Cars and couches")), "http://www.url.de", "imageName",
                        new ArrayList<>(List.of(new Project("projectid1", "andi324", "title1", "description1", "projectimageurl1", "projectimage1", "teaser1"))),
                        new ArrayList<>(List.of(new Message("messageid1", Instant.parse("2020-10-24T14:15:00Z"), "subject1", "message1", "birgit116", "andi324"))),
                        new ArrayList<>(List.of(new Message("messageid2", Instant.parse("2020-10-24T14:15:00Z"), "subject2", "message2", "andi324", "birgit116")))),
                new Profile("birgit116","Birgit", LocalDate.of(1985, 9, 9),
                        "Hamburg", new ArrayList<>(List.of("Nails")), "http://www.url.de", "imageName",
                        new ArrayList<>(List.of(new Project("projectid2", "birgit116", "title2", "description2", "projectimageurl2", "projectimage2", "teaser2"))),
                        new ArrayList<>(List.of(new Message("messageid2", Instant.parse("2020-10-24T14:15:00Z"), "subject2", "message2", "andi324", "birgit116"))),
                        new ArrayList<>(List.of(new Message("messageid1", Instant.parse("2020-10-24T14:15:00Z"), "subject1", "message1", "birgit116", "andi324")))),
                new Profile("kai", "Kai", LocalDate.of(1991, 11, 5), "Moers", new ArrayList<>(List.of("coding")), "http://www.url.de", "imageName",
                        new ArrayList<>(List.of(new Project("projectid3", "kai", "title3", "description3", "projectimageurl3", "projectimage3", "teaser3"))),
                        new ArrayList<>(List.of(new Message("messageid1", Instant.parse("2020-10-24T14:15:00Z"), "subject1", "message1", "birgit116", "kai"))),
                        new ArrayList<>(List.of(new Message("messageid2", Instant.parse("2020-10-24T14:15:00Z"), "subject2", "message2", "kai", "birgit116"))))
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
    public void testGetMappingShouldReturnAllProjects() throws MalformedURLException {

        //Given
        List<Project> projectList = List.of(
                new Project("id1", "kai", "title1", "description1", "http://www.url.de", "imagename", "teaser1"),
                new Project("id2", "andi324", "title2", "description2", "http://www.url.de", "imagename", "teaser2"),
                new Project("id3", "monika", "title3", "description3", "http://www.url.de", "imagename", "teaser3")
        );

        when(expirationUtils.getExpirationTime()).thenReturn(expiration);
        when(s3ClientUtils.getBucketName()).thenReturn(bucketName);
        when(s3Client.generatePresignedUrl(bucketName, "imagename", expiration, com.amazonaws.HttpMethod.GET)).thenReturn(new URL("http://www.url.de"));

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
        ProjectDto projectDto = new ProjectDto("some title", "some description", "imagename", "someteaser");
        Project expected = new Project("some-id", "kai", "some title", "some description", "", "imagename", "someteaser");
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
        Project expectedProject = new Project("id1", "kai", "some title", "some description", null, "imagename", "someteaser");
        ProjectDto projectDto = new ProjectDto("some title", "some description", "imagename", "someteaser");

        //When
        HttpEntity<ProjectDto> entity = getValidAuthorizationEntity(projectDto);
        ResponseEntity<Project> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Project.class);
        Optional<Project> savedProject = projectDb.findById(id);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(expectedProject));
    }

    @Test
    public void testUpdateProjectShouldThrow404WhenIdNotFound() {

        //Given
        String id = "some-unknown-id";
        String url = getProjectsUrl() + "/" + id;
        ProjectDto projectDto = new ProjectDto("some title", "some description", "imagename", "someteaser");

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
        ProjectDto projectDto = new ProjectDto("some title", "some description", "imagename", "someteaser");

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
