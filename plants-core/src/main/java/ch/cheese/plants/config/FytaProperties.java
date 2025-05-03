package ch.cheese.plants.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fyta")
@Getter
@Setter
public class FytaProperties {
    private String apiBaseUrl;
    private Auth auth = new Auth();

    @Getter
    @Setter
    public static class Auth {
        private String email;
        private String password;
    }
}
