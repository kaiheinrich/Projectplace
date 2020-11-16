package de.kaiheinrich.projectplace.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document (collection = "profile")
public class Profile {

    @Id
    private String username;
    private String name;
    private String age;
    private String location;
    private String skills;
}
