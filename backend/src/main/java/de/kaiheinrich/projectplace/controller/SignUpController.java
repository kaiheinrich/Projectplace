package de.kaiheinrich.projectplace.controller;

import de.kaiheinrich.projectplace.model.ProjectplaceUser;
import de.kaiheinrich.projectplace.service.SignUpService;
import de.kaiheinrich.projectplace.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("auth/signup")
public class SignUpController {

    private final SignUpService signUpService;
    private final PasswordUtils passwordUtils;

    @Autowired
    public SignUpController(SignUpService signUpService, PasswordUtils passwordUtils) {
        this.signUpService = signUpService;
        this.passwordUtils = passwordUtils;
    }

    @PostMapping
    public String signUp(@RequestBody ProjectplaceUser projectplaceUser) {

        Optional<String> username = signUpService.signUp(projectplaceUser);

        if(username.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists!");
        }
        if(!passwordUtils.validatePassword(projectplaceUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid password!");
        }

        return username.get();
    }
}
