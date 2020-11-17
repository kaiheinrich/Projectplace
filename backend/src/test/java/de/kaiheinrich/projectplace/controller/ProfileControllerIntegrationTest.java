
package de.kaiheinrich.projectplace.controller;

import de.kaiheinrich.projectplace.db.ProfileMongoDb;
import de.kaiheinrich.projectplace.dto.ProfileDto;
import de.kaiheinrich.projectplace.model.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProfileControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    ProfileMongoDb profileMongoDb;

    @BeforeEach
    public void setupProfileDb() {
        profileMongoDb.deleteAll();
        profileMongoDb.saveAll(List.of(
                new Profile("andi324","Andreas", LocalDate.of(1900, 10, 24), "Berlin", List.of("Cars and couches")),
                new Profile("birgit116","Birgit", LocalDate.of(1985, 9, 9), "Hamburg", List.of("Nails"))
        ));
    }

    private String getProfilesUrl() {
        return "http://localhost:" + port + "/api/profiles";
    }

    @Test
    public void testGetMapping() {

        //Given
        List<Profile> profileList = List.of(
                new Profile("andi324","Andreas", LocalDate.of(1900, 10, 24), "Berlin", List.of("Cars and couches")),
                new Profile("birgit116","Birgit", LocalDate.of(1985, 9, 9), "Hamburg", List.of("Nails"))
        );

        //When
        ResponseEntity<Profile[]> response = restTemplate.getForEntity(getProfilesUrl(), Profile[].class);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(profileList.toArray()));
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
        HttpEntity<ProfileDto> entity = new HttpEntity<>(profileDto);
        ResponseEntity<Profile> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Profile.class);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    void updateProfileShouldUpdateExistingProfile() {

        //Given
        String url = getProfilesUrl() + "/andi324";

        ProfileDto profileDto = ProfileDto.builder()
                .name("Andi der coole")
                .birthday(LocalDate.of(1977, 1, 1))
                .location("Toronto")
                .skills(List.of("minigolf"))
                .build();

        Profile updatedProfile = new Profile(
                "andi324","Andi der coole", LocalDate.of(1977, 1, 1), "Toronto", List.of("minigolf")
        );


        //When
        HttpEntity<ProfileDto> entity = new HttpEntity<>(profileDto);
        ResponseEntity<Profile> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Profile.class);

        //Then
        Profile savedProfile = profileMongoDb.findById("andi324").orElseThrow();

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(updatedProfile));
        assertThat(savedProfile, is(updatedProfile));
    }
}
