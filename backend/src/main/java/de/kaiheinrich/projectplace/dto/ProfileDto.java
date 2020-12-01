package de.kaiheinrich.projectplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDto {

    private String name;
    private LocalDate birthday;
    private String location;
    private List<String> skills;
    private String imageUrl;
}
