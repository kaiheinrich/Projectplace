package de.kaiheinrich.projectplace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {

    private String username;
    private String password;
    private String name;
    private LocalDate birthday;
}
