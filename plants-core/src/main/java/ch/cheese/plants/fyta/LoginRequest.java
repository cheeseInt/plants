package ch.cheese.plants.fyta;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    // Getter und Setter
    private String email;
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
