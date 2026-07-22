package ch.cheese.plants.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fyta")
@Getter
@Setter
public class FytaProperties {
    private String apiBaseUrl;
    private String email;
    private String password;
    private Boolean startLoad;
}