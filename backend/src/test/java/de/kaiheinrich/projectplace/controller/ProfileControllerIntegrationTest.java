
package de.kaiheinrich.projectplace.controller;

import de.kaiheinrich.projectplace.db.ProfileMongoDb;
import de.kaiheinrich.projectplace.db.UserMongoDb;
import de.kaiheinrich.projectplace.dto.ProfileDto;
import de.kaiheinrich.projectplace.model.Profile;
import de.kaiheinrich.projectplace.model.ProjectplaceUser;
import de.kaiheinrich.projectplace.security.dto.LoginDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "jwt.secretkey=someverycoolsecretkey")
class ProfileControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    ProfileMongoDb profileDb;

    @Autowired
    private UserMongoDb userDb;

    @BeforeEach
    public void setupProfileDb() {
        profileDb.deleteAll();
        profileDb.saveAll(List.of(
                new Profile("andi324","Andreas", LocalDate.of(1900, 10, 24), "Berlin", List.of("Cars and couches")),
                new Profile("birgit116","Birgit", LocalDate.of(1985, 9, 9), "Hamburg", List.of("Nails")),
                new Profile("kai", "Kai", LocalDate.of(1991, 11, 5), "Moers", List.of("coding"))
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
    public void testGetMapping() {

        //Given
        List<Profile> profileList = List.of(
                new Profile("andi324","Andreas", LocalDate.of(1900, 10, 24), "Berlin", List.of("Cars and couches")),
                new Profile("birgit116","Birgit", LocalDate.of(1985, 9, 9), "Hamburg", List.of("Nails")),
                new Profile("kai", "Kai", LocalDate.of(1991, 11, 5), "Moers", List.of("coding"))
        );

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
        ResponseEntity<String> response = restTemplate.getForEntity(getProfilesUrl(), String.class);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void testPutMappingShouldReturn404WhenUsernameIsNotKnown() {

        //Given
        String url = getProfilesUrl() + "/werner7";

        ProfileDto profileDto = ProfileDto.builder()
                .name("Werner")
                .birthday(LocalDate.of(1887, 4, 5))
                .location("Münster")
                .skills(List.of("cooking"))
                .build();

        //When
        HttpEntity<ProfileDto> entity = getValidAuthorizationEntity(profileDto);
        ResponseEntity<Profile> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Profile.class);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    void updateProfileShouldUpdateExistingProfile() {

        //Given
        String url = getProfilesUrl() + "/kai";

        ProfileDto profileDto = ProfileDto.builder()
                .name("Andi der coole")
                .birthday(LocalDate.of(1977, 1, 1))
                .location("Toronto")
                .skills(List.of("minigolf"))
                .build();

        Profile updatedProfile = new Profile(
                "kai","Andi der coole", LocalDate.of(1977, 1, 1), "Toronto", List.of("minigolf")
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
        ResponseEntity<Profile> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Profile.class);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }
}
