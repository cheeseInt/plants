package ch.cheese.plants;

import ch.cheese.plants.config.FytaProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(FytaProperties.class)
public class PlantsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlantsApplication.class, args);
    }

}
