package de.kaiheinrich.projectplace.security;

import de.kaiheinrich.projectplace.db.UserMongoDb;
import de.kaiheinrich.projectplace.model.Profile;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "jwt.secretkey=somesecretkey")
class JwtAuthFilterTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserMongoDb userDb;

    @Autowired
    private TestRestTemplate restTemplate;

    private final String secretkey = "somesecretkey";

    private String getProfilesUrl() {
        return "http://localhost:" + port + "/api/profiles";
    }

    @Test
    public void getWithValidJwtTokenShouldReturn200() {

        //Given
        String token = Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject("kai")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(Duration.ofHours(2))))
                .signWith(SignatureAlgorithm.HS256, secretkey)
                .compact();

        //When
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Profile[]> response = restTemplate.exchange(getProfilesUrl(), HttpMethod.GET, entity, Profile[].class);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void getWithExpiredJwtTokenShouldReturn403Forbidden() {

        //Given
        String token = Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject("kai")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().minus(Duration.ofSeconds(1))))
                .signWith(SignatureAlgorithm.HS256, secretkey)
                .compact();

        //When
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(getProfilesUrl(), HttpMethod.GET, entity, String.class);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void getWithValidJwtTokenAndWrongKeyShouldReturn403Forbidden() {

        //Given
        String token = Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject("kai")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(Duration.ofHours(2))))
                .signWith(SignatureAlgorithm.HS256, "anothercoolkey")
                .compact();

        //When
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(getProfilesUrl(), HttpMethod.GET, entity, String.class);

        //Then
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }
}