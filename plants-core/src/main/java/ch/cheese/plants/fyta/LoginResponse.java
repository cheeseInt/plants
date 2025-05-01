package ch.cheese.plants.fyta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponse {

    @JsonProperty("access_token")
    private String accessToken;

}
