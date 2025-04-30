package ch.cheese.plants;

import lombok.Data;

@Data
public class Garden {
    private int id;
    private Integer index;
    private String garden_name;
    private String origin_path;
    private String thumb_path;
    private String mac_address;
}
