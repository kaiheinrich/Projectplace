package de.kaiheinrich.projectplace.utils;

import org.springframework.stereotype.Component;

@Component
public class PasswordUtils {

    public boolean validatePassword(String password) {
        return isLongEnough(password)&& containsNumbers(password) && containsUpperCases(password) && containsLowerCases(password);
    }

    public boolean containsLowerCases(String password) {
        return password.matches(".*[a-z].*");
    }

    public boolean containsUpperCases(String password) {
        return password.matches(".*[A-Z].*");
    }

    public boolean containsNumbers(String password) {
        return password.matches(".*[0-9].*");
    }

    public boolean isLongEnough(String password) {
        return password.length()>=8;
    }
}
