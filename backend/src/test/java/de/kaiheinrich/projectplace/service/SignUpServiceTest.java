package de.kaiheinrich.projectplace.service;

import de.kaiheinrich.projectplace.db.ProfileMongoDb;
import de.kaiheinrich.projectplace.db.UserMongoDb;
import de.kaiheinrich.projectplace.dto.SignUpDto;
import de.kaiheinrich.projectplace.model.Profile;
import de.kaiheinrich.projectplace.model.ProjectplaceUser;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

class SignUpServiceTest {

    private UserMongoDb userDb = mock(UserMongoDb.class);
    private ProfileMongoDb profileDb = mock(ProfileMongoDb.class);
    private BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
    private SignUpService signUpService = new SignUpService(userDb, profileDb);

    @Test
    public void testSignUpShouldReturnOptionalOfNewUser() {

        //Given
        SignUpDto signUpDto = new SignUpDto("kai", "Password123", "Kai", LocalDate.of(1991, 11, 5));
        Profile newProfile = new Profile("kai", "Kai", LocalDate.of(1991,11,5), "", List.of(), "", "", List.of(), List.of(), List.of());
        when(userDb.findById("kai")).thenReturn(Optional.empty());

        //When
        Optional<String> newUsername = signUpService.signUp(signUpDto);

        //Then
        assertThat(newUsername, is(Optional.of("kai")));
        verify(profileDb).save(newProfile);
    }

    @Test
    public void testSignUpShouldReturnEmptyOptionalWhenUsernameAlreadyExists() {

        //Given
        SignUpDto signUpDto = new SignUpDto("kai", "Password123", "Kai", LocalDate.of(1991, 11, 5));
        ProjectplaceUser user = new ProjectplaceUser("kai", "some-hashed-password");
        when(userDb.findById("kai")).thenReturn(Optional.of(user));

        //When
        Optional<String> signup = signUpService.signUp(signUpDto);

        //Then
        assertThat(signup, is(Optional.empty()));
    }
}