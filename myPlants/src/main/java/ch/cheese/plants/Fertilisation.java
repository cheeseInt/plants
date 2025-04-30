package ch.cheese.plants;

import lombok.Data;

@Data
public class Fertilisation {
    private String last_fertilised_at;
    private String fertilise_at;
    private boolean was_repotted;
}
