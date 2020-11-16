package de.kaiheinrich.projectplace.service;

import de.kaiheinrich.projectplace.db.ProfileMongoDb;
import de.kaiheinrich.projectplace.model.Profile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProfileServiceTest {

    final ProfileMongoDb profileDb = mock(ProfileMongoDb.class);
    final ProfileService profileService = new ProfileService(profileDb);

    @Test
    @DisplayName("The getProfiles method should return all profiles")
    void getAllProfilesTest() {

        //Given
        List<Profile> profiles = List.of(
                new Profile("andi324","Andreas", "24", "Berlin", "Cars and couches"),
                new Profile("birgit116","Birgit", "36", "Hamburg", "Nails")
        );

        when(profileDb.findAll()).thenReturn(profiles);

        //When
        List<Profile> resultProfiles = profileDb.findAll();

        //Then
        assertThat(resultProfiles, containsInAnyOrder(profiles.toArray()));
    }


    @Test
    @DisplayName("The getProfileByUsername Test should return the correct user")
    void getProfileByUsernameTest() {

        //Given
        String searchedUsername = "andi324";

        Profile searchedProfile = new Profile("andi324","Andreas", "24", "Berlin", "Cars and couches");

        when(profileDb.findById(searchedUsername)).thenReturn(Optional.of(searchedProfile));

        //When
        Optional<Profile> resultProfile = Optional.of(new Profile("andi324","Andreas", "24", "Berlin", "Cars and couches"));

        //Then
        assertThat(resultProfile.get(), is(new Profile("andi324","Andreas", "24", "Berlin", "Cars and couches")));
    }

    @Test
    @DisplayName("The getProfileByUsername Test should return a 404 Exception when an unknown user is searched")
    void getProfileByUsernameTestWithUnknownUsername() {

        //Given
        String searchedUsername = "wolle787";

        when(profileDb.findById(searchedUsername)).thenReturn(Optional.empty());

        try {
            profileService.getProfileByUsername(searchedUsername);
            fail("No exception thrown!");
        } catch (ResponseStatusException exception) {
            assertThat(exception.getStatus(), is(HttpStatus.NOT_FOUND));
        }
    }

    @Test
    void updateProfile() {
    }
}