package de.kaiheinrich.projectplace.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document (collection = "profile")
public class Profile {

    @Id
    private String username;
    private String name;
    private LocalDate birthday;
    private String location;
    private List<String> skills;
    private String imageUrl;
    private String imageName;
    private List<Project> projects;
    private List<Message> receivedMessages;
    private List<Message> sentMessages;
}
