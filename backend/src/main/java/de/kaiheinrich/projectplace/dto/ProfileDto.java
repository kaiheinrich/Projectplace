package de.kaiheinrich.projectplace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {

    private String name;
    private String age;
    private String location;
    private String skills;
}
