package de.kaiheinrich.projectplace.service;

import de.kaiheinrich.projectplace.db.ProfileMongoDb;
import de.kaiheinrich.projectplace.dto.ProfileDto;
import de.kaiheinrich.projectplace.model.Profile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

class ProfileServiceTest {

    final ProfileMongoDb profileDb = mock(ProfileMongoDb.class);
    final ProfileService profileService = new ProfileService(profileDb);

    @Test
    @DisplayName("The getProfiles method should return all profiles")
    public void getAllProfilesTest() {

        //Given
        List<Profile> profiles = List.of(
                new Profile("andi324","Andreas", "24", "Berlin", List.of("Cars and couches")),
                new Profile("birgit116","Birgit", "36", "Hamburg", List.of("Nails"))
        );

        when(profileDb.findAll()).thenReturn(profiles);

        //When
        List<Profile> resultProfiles = profileDb.findAll();

        //Then
        assertThat(resultProfiles, containsInAnyOrder(profiles.toArray()));
    }


    @Test
    @DisplayName("The getProfileByUsername Test should return the correct user")
    public void getProfileByUsernameTest() {

        //Given
        String searchedUsername = "andi324";

        Profile searchedProfile = new Profile("andi324","Andreas", "24", "Berlin", List.of("Cars and couches"));

        when(profileDb.findById("andi324")).thenReturn(Optional.of(searchedProfile));

        //When
        Profile resultProfile = profileDb.findById(searchedUsername).orElseThrow();

        //Then
        assertThat(resultProfile, is(new Profile("andi324","Andreas", "24", "Berlin", List.of("Cars and couches"))));
    }

    @Test
    @DisplayName("The getProfileByUsername Test should return an empty optional")
    public void getProfileByUsernameTestWithUnknownUsername() {

        //Given
        String searchedUsername = "wolle787";

        when(profileDb.findById(searchedUsername)).thenReturn(Optional.empty());

        //When
        Optional<Profile> result = profileDb.findById(searchedUsername);

        //Then
        assertThat(result, is(Optional.empty()));
    }

    @Test
    public void updateProfileTest() {

        //Given
        String username = "Günni";

        ProfileDto profileDto = new ProfileDto(
                "Horst",
                "60",
                "Würzburg",
                List.of("drinking")
        );

        Profile previousProfile = new Profile(
                "Günni",
                "Günther",
                "41",
                "Pfaffenhofen",
                List.of("smoke")
        );

        Profile updatedProfile = new Profile(
                "Günni",
                "Horst",
                "60",
                "Würzburg",
                List.of("drinking")
        );

        when(profileDb.findById(username)).thenReturn(Optional.of(previousProfile));
        when(profileDb.save(updatedProfile)).thenReturn(updatedProfile);

        //When
        Profile result = profileService.updateProfile(profileDto, "Günni");

        //Then
        assertThat(result, is(updatedProfile));
        verify(profileDb).save(updatedProfile);
    }
}