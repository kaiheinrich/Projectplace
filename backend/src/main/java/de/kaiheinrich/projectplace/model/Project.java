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
@Document(collection = "project")
public class Project {

    @Id
    private String id;
    private String projectOwner;
    private String title;
    private String description;
    private String imageUrl;
    private String imageName;
    private String teaser;
}
