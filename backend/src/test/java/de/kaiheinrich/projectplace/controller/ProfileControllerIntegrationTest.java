package de.kaiheinrich.projectplace.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.kaiheinrich.projectplace.db.ProfileMongoDb;
import de.kaiheinrich.projectplace.db.UserMongoDb;
import de.kaiheinrich.projectplace.dto.MessageDto;
import de.kaiheinrich.projectplace.dto.ProfileDto;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "jwt.secretkey=someverycoolsecretkey")
class ProfileControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProfileMongoDb profileDb;

    @Autowired
    private UserMongoDb userDb;

    @MockBean
    private AmazonS3 s3Client;

    @MockBean
    private AmazonS3ClientUtils s3ClientUtils;

    @MockBean
    private DateExpirationUtils expirationUtils;

    @MockBean
    private TimestampUtils timestampUtils;

    @MockBean
    private IdUtils idUtils;

    private String bucketName = "testbucketname";
    private Date expiration = new Date(2030, Calendar.JANUARY,1);

    @BeforeEach
    public void setup() {
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

    private String getProfilesUrl() {
        return "http://localhost:" + port + "/api/profiles";
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
    public void testGetMapping() throws MalformedURLException {

        //Given
        List<Profile> profileList = List.of(
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
        );

        when(expirationUtils.getExpirationTime()).thenReturn(expiration);
        when(s3ClientUtils.getBucketName()).thenReturn(bucketName);
        when(s3Client.generatePresignedUrl(bucketName, "imageName", expiration, com.amazonaws.HttpMethod.GET)).thenReturn(new URL("http://www.url.de"));

        //When
        HttpEntity<Void> entity = getValidAuthorizationEntity(null);
        ResponseEntity<Profile[]> response = restTemplate.exchange(getProfilesUrl(), HttpMethod.GET, entity, Profile[].class);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(profileList.toArray()));
    }

    @Test
    public void testGetMappingShouldReturnForbiddenWithoutToken() {

        //When
        ResponseEntity<Void> response = restTemplate.getForEntity(getProfilesUrl(), Void.class);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    void updateProfileShouldUpdateExistingProfile() {

        //Given
        String url = getProfilesUrl() + "/kai";

        ProfileDto profileDto = ProfileDto.builder()
                .name("Kai der zweite")
                .birthday(LocalDate.of(1977, 1, 1))
                .location("Toronto")
                .skills(new ArrayList<>(List.of("minigolf")))
                .imageName("super-image")
                .build();

        Profile updatedProfile = new Profile(
                "kai","Kai der zweite", LocalDate.of(1977, 1, 1), "Toronto", new ArrayList<>(List.of("minigolf")),
                null, "super-image", new ArrayList<>(List.of(new Project("projectid3", "kai", "title3", "description3", "projectimageurl3", "projectimage3", "teaser3"))),
                new ArrayList<>(List.of(new Message("messageid1", Instant.parse("2020-10-24T14:15:00Z"), "subject1", "message1", "birgit116", "kai"))),
                new ArrayList<>(List.of(new Message("messageid2", Instant.parse("2020-10-24T14:15:00Z"), "subject2", "message2", "kai", "birgit116")))
        );


        //When
        HttpEntity<ProfileDto> entity = getValidAuthorizationEntity(profileDto);
        ResponseEntity<Profile> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Profile.class);

        //Then
        Profile savedProfile = profileDb.findById("kai").orElseThrow();

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(updatedProfile));
        assertThat(savedProfile, is(updatedProfile));
    }

    @Test
    public void testPutMappingShouldReturn403WhenUserUpdatesOtherProfile() {

        //Given
        String url = getProfilesUrl() + "/andi324";

        ProfileDto profileDto = ProfileDto.builder()
                .name("Andi der coole")
                .birthday(LocalDate.of(1977, 1, 1))
                .location("Toronto")
                .skills(List.of("minigolf"))
                .build();

        //When
        HttpEntity<ProfileDto> entity = getValidAuthorizationEntity(profileDto);
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void testPostMappingShouldSendMessage() {

        //Given
        String url = getProfilesUrl() + "/message/andi324";
        MessageDto messageDto = MessageDto.builder()
                .subject("Welcome")
                .message("Hello")
                .sender("kai")
                .recipient("andi324")
                .build();

        when(idUtils.generateId()).thenReturn("some-id");
        when(timestampUtils.generateTimestampEpochSeconds()).thenReturn(Instant.parse("2020-10-24T14:15:00Z"));

        Message expectedMessage = Message.builder()
                .id("some-id")
                .timestamp(Instant.parse("2020-10-24T14:15:00Z"))
                .subject("Welcome")
                .message("Hello")
                .sender("kai")
                .recipient("andi324")
                .build();

        //When
        HttpEntity<MessageDto> entity = getValidAuthorizationEntity(messageDto);
        ResponseEntity<Message> response = restTemplate.exchange(url, HttpMethod.POST, entity, Message.class);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(expectedMessage));
    }

    @Test
    public void testPostMappingShouldReturnForbiddenWhenSenderAndPrincipalDoNotMatch() {

        //Given
        String url = getProfilesUrl() + "/message/andi324";
        MessageDto messageDto = MessageDto.builder()
                .subject("Welcome")
                .message("Hello")
                .sender("horst")
                .recipient("andi324")
                .build();

        //When
        HttpEntity<MessageDto> entity = getValidAuthorizationEntity(messageDto);
        ResponseEntity<Message> response = restTemplate.exchange(url, HttpMethod.POST, entity, Message.class);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }
}

