package de.kaiheinrich.projectplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDto {

    private String name;
    private String age;
    private String location;
    private List<String> skills;
}
