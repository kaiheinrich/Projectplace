package de.kaiheinrich.projectplace.controller;

import de.kaiheinrich.projectplace.db.UserMongoDb;
import de.kaiheinrich.projectplace.dto.SignUpDto;
import de.kaiheinrich.projectplace.model.ProjectplaceUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "jwt.secretkey=somesecrettoken")
class SignUpControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserMongoDb userDb;

    @BeforeEach
    public void setupUserDb() {
        userDb.deleteAll();
        String encodedPassword = new BCryptPasswordEncoder().encode("Password123");
        userDb.save(new ProjectplaceUser("kai", encodedPassword));
    }

    private String getSignUpUrl() {
        return "http://localhost:" + port + "/auth/signup";
    }

    @Test
    public void testPostMappingShouldSignUpNewUserToProjectplace() {

        //Given
        SignUpDto signUpDto = new SignUpDto("andi", "Password123", "Kai", LocalDate.of(1991, 11, 5));

        //When
        ResponseEntity<String> response = restTemplate.postForEntity(getSignUpUrl(), signUpDto, String.class);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is("andi"));
    }

    @Test
    public void testPostMappingShouldReturnBadRequestWhenUserExists() {

        //Given
        SignUpDto signUpDto = new SignUpDto("kai", "Password123", "Kai", LocalDate.of(1991, 11, 5));

        //When
        ResponseEntity<String> response = restTemplate.postForEntity(getSignUpUrl(), signUpDto, String.class);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @ParameterizedTest(name = "Password {0} should lead to bad request")
    @CsvSource({
            "PASSWORD123",
            "password123",
            "Password",
            "Pass1"
    })
    public void testPostMappingShouldReturnBadRequestWhenPasswordDoesNotMatchCriteria(String password) {

        //Given
        SignUpDto signUpDto = new SignUpDto("kai", password, "Kai", LocalDate.of(1991, 11, 5));

        //When
        ResponseEntity<String> response = restTemplate.postForEntity(getSignUpUrl(), signUpDto, String.class);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }
}