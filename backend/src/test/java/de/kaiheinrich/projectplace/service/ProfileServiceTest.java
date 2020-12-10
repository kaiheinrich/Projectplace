package de.kaiheinrich.projectplace.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import de.kaiheinrich.projectplace.db.ProfileMongoDb;
import de.kaiheinrich.projectplace.dto.MessageDto;
import de.kaiheinrich.projectplace.dto.ProfileDto;
import de.kaiheinrich.projectplace.model.Message;
import de.kaiheinrich.projectplace.model.Profile;
import de.kaiheinrich.projectplace.model.Project;
import de.kaiheinrich.projectplace.utils.AmazonS3ClientUtils;
import de.kaiheinrich.projectplace.utils.DateExpirationUtils;
import de.kaiheinrich.projectplace.utils.IdUtils;
import de.kaiheinrich.projectplace.utils.TimestampUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

class ProfileServiceTest {

    private final ProfileMongoDb profileDb = mock(ProfileMongoDb.class);
    private final AmazonS3 s3Client = mock(AmazonS3.class);
    private final AmazonS3ClientUtils s3ClientUtils = mock(AmazonS3ClientUtils.class);
    private final IdUtils idUtils = mock(IdUtils.class);
    private final TimestampUtils timestampUtils = mock(TimestampUtils.class);
    private final DateExpirationUtils expirationUtils = mock(DateExpirationUtils.class);
    private final ProfileService profileService = new ProfileService(
            profileDb, s3Client, s3ClientUtils, idUtils, timestampUtils, expirationUtils);


    @Test
    @DisplayName("The getProfiles method should return all profiles")
    public void getAllProfilesTest() throws MalformedURLException {

        //Given
        List<Profile> profiles = List.of(
                new Profile("andi324","Andreas", LocalDate.of(1900, 10, 24),
                        "Berlin", new ArrayList<>(List.of("Cars and couches")), "http://www.url.de", "imageName",
                        new ArrayList<>(List.of(new Project("projectid1", "andi324", "title1", "description1", "projectimageurl1", "projectimage1", "teaser1"))),
                        new ArrayList<>(List.of(new Message("messageid1", Instant.parse("2020-10-24T14:15:00Z"), "subject1", "message1", "birgit116", "andi324"))),
                        new ArrayList<>(List.of(new Message("messageid2", Instant.parse("2020-10-24T14:15:00Z"), "subject2", "message2", "andi324", "birgit116")))),
                new Profile("birgit116","Birgit", LocalDate.of(1985, 9, 9),
                        "Hamburg", new ArrayList<>(List.of("Nails")), "http://www.url.de", "imageName",
                        new ArrayList<>(List.of(new Project("projectid2", "birgit116", "title2", "description2", "projectimageurl2", "projectimage2", "teaser2"))),
                        new ArrayList<>(List.of(new Message("messageid2", Instant.parse("2020-10-24T14:15:00Z"), "subject2", "message2", "andi324", "birgit116"))),
                        new ArrayList<>(List.of(new Message("messageid1", Instant.parse("2020-10-24T14:15:00Z"), "subject1", "message1", "birgit116", "andi324"))))
        );
        Date expiration = new Date(2030, Calendar.JANUARY,1);

        when(profileDb.findAll()).thenReturn(profiles);
        when(expirationUtils.getExpirationTime()).thenReturn(expiration);
        when(s3ClientUtils.getBucketName()).thenReturn("test-bucket");
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest("test-bucket", "imageName")
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);
        when(s3Client.generatePresignedUrl("test-bucket", "imageName", expiration, HttpMethod.GET)).thenReturn(new URL("http://www.url.de"));


        //When
        List<Profile> resultProfiles = profileService.getProfiles();

        //Then
        assertThat(resultProfiles, containsInAnyOrder(profiles.toArray()));
    }

    @Test
    @DisplayName("The getProfileByUsername Test should return the correct user")
    public void getProfileByUsernameTest() {

        //Given
        String searchedUsername = "andi324";

        Profile searchedProfile = new Profile("andi324","Andreas", LocalDate.of(1900, 10, 24),
                "Berlin", new ArrayList<>(List.of("Cars and couches")), "http://www.url.de", "imageName",
                new ArrayList<>(List.of(new Project("projectid1", "andi324", "title1", "description1", "projectimageurl1", "projectimage1", "teaser1"))),
                new ArrayList<>(List.of(new Message("messageid1", Instant.parse("2020-10-24T14:15:00Z"), "subject1", "message1", "birgit116", "andi324"))),
                new ArrayList<>(List.of(new Message("messageid2", Instant.parse("2020-10-24T14:15:00Z"), "subject2", "message2", "andi324", "birgit116"))));

        when(profileDb.findById("andi324")).thenReturn(Optional.of(searchedProfile));

        //When
        Profile resultProfile = profileService.getProfileByUsername(searchedUsername).orElseThrow();

        //Then
        assertThat(resultProfile, is(new Profile("andi324","Andreas", LocalDate.of(1900, 10, 24),
                "Berlin", new ArrayList<>(List.of("Cars and couches")), "http://www.url.de", "imageName",
                new ArrayList<>(List.of(new Project("projectid1", "andi324", "title1", "description1", "projectimageurl1", "projectimage1", "teaser1"))),
                new ArrayList<>(List.of(new Message("messageid1", Instant.parse("2020-10-24T14:15:00Z"), "subject1", "message1", "birgit116", "andi324"))),
                new ArrayList<>(List.of(new Message("messageid2", Instant.parse("2020-10-24T14:15:00Z"), "subject2", "message2", "andi324", "birgit116"))))));
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
        String username = "andi324";

        ProfileDto profileDto = new ProfileDto(
                "Horst",
                LocalDate.of(1950, 5, 27),
                "Würzburg",
                List.of("drinking"),
                "newImage"
        );

        Profile previousProfile = new Profile("andi324","Andreas", LocalDate.of(1900, 10, 24),
                "Berlin", List.of("Cars and couches"), "http://www.url.de", "imageName",
                List.of(new Project("projectid1", "andi324", "title1", "description1", "projectimageurl1", "projectimage1", "teaser1")),
                List.of(new Message("messageid1", Instant.parse("2020-10-24T14:15:00Z"), "subject1", "message1", "birgit116", "andi324")),
                List.of(new Message("messageid2", Instant.parse("2020-10-24T14:15:00Z"), "subject2", "message2", "andi324", "birgit116")));

        Profile updatedProfile = new Profile("andi324","Horst", LocalDate.of(1950, 5, 27),
                "Würzburg", List.of("drinking"), null, "newImage",
                List.of(new Project("projectid1", "andi324", "title1", "description1", "projectimageurl1", "projectimage1", "teaser1")),
                List.of(new Message("messageid1", Instant.parse("2020-10-24T14:15:00Z"), "subject1", "message1", "birgit116", "andi324")),
                List.of(new Message("messageid2", Instant.parse("2020-10-24T14:15:00Z"), "subject2", "message2", "andi324", "birgit116")));

        when(profileDb.findById(username)).thenReturn(Optional.of(previousProfile));
        when(profileDb.save(updatedProfile)).thenReturn(updatedProfile);

        //When
        Profile result = profileService.updateProfile(profileDto, username);

        //Then
        assertThat(result, is(updatedProfile));
        verify(profileDb).save(updatedProfile);
    }

    @Test
    public void testSendMessageShouldSaveMessagesToSenderAndRecipient() {

        //Given
        String sender = "andi324";
        String recipient = "birgit116";
        String expectedId = "some-id";
        Instant timestamp = Instant.parse("2020-10-24T14:15:00Z");
        MessageDto messageDto = new MessageDto(
                "hey", "hello", sender, recipient
        );
        Message expected = new Message(
                expectedId, timestamp, "hey", "hello", sender, recipient
        );
        Profile senderProfile = new Profile("andi324","Andreas", LocalDate.of(1900, 10, 24),
                "Berlin", List.of("Cars and couches"), "http://www.url.de", "imageName",
                new ArrayList<>(List.of(new Project("projectid1", "andi324", "title1", "description1", "projectimageurl1", "projectimage1", "teaser1"))),
                new ArrayList<>(List.of(new Message("messageid1", Instant.parse("2020-10-24T14:15:00Z"), "subject1", "message1", "birgit116", "andi324"))),
                new ArrayList<>(List.of(new Message("messageid2", Instant.parse("2020-10-24T14:15:00Z"), "subject2", "message2", "andi324", "birgit116"))));

        Profile recipientProfile = new Profile("birgit116","Birgit", LocalDate.of(1985, 9, 9),
                "Hamburg", List.of("Nails"), "http://www.url.de", "imageName",
                new ArrayList<>(List.of(new Project("projectid2", "birgit116", "title2", "description2", "projectimageurl2", "projectimage2", "teaser2"))),
                new ArrayList<>(List.of(new Message("messageid2", Instant.parse("2020-10-24T14:15:00Z"), "subject2", "message2", "andi324", "birgit116"))),
                new ArrayList<>(List.of(new Message("messageid1", Instant.parse("2020-10-24T14:15:00Z"), "subject1", "message1", "birgit116", "andi324"))));

        when(idUtils.generateId()).thenReturn(expectedId);
        when(timestampUtils.generateTimestampEpochSeconds()).thenReturn(timestamp);
        when(profileDb.findById(sender)).thenReturn(Optional.of(senderProfile));
        when(profileDb.findById(recipient)).thenReturn(Optional.of(recipientProfile));

        //When
        Message result = profileService.sendMessage(sender, recipient, messageDto);

        //Then
        assertThat(result, is(expected));
    }
}
