package de.kaiheinrich.projectplace.controller;

import de.kaiheinrich.projectplace.db.UserMongoDb;
import de.kaiheinrich.projectplace.model.ProjectplaceUser;
import de.kaiheinrich.projectplace.security.dto.LoginDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "jwt.secretkey=someverycoolsecretkey")
class LoginControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserMongoDb userDb;

    @BeforeEach
    public void setupUser() {
        userDb.deleteAll();
        String password = "password";
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        userDb.save(new ProjectplaceUser("kai", encodedPassword));
    }

    private final String secretkey = "someverycoolsecretkey";

    private String getLoginUrl() {
        return "http://localhost:" + port + "/auth/login";
    }

    @Test
    public void loginWithValidCredentialsShouldReturnJwtToken() {

        //Given
        LoginDto loginDto = new LoginDto(
                "kai",
                "password"
        );

        //When
        ResponseEntity<String> response = restTemplate.postForEntity(getLoginUrl(), loginDto, String.class);
        String token = response.getBody();
        Claims claims = Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token).getBody();

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(claims.getSubject(), is("kai"));
        assertThat(claims.getExpiration().after(new Date()), is(true));
    }

    @Test
    public void loginWithInvalidCredentialsShouldReturnForbidden() {

        //Given
        LoginDto loginDto = new LoginDto(
                "werner",
                "werner123"
        );

        //When
        ResponseEntity<Void> response = restTemplate.postForEntity(getLoginUrl(), loginDto, Void.class);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }
}
