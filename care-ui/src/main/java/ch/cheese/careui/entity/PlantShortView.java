package ch.cheese.careui.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "plants", schema = "fytadb")
@Data
public class PlantShortView {

    @Id
    private Integer id;

    private String nickname;
}
