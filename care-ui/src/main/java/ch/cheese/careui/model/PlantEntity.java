package ch.cheese.careui.model;

import lombok.Data;

@Data
public class PlantEntity {
    private Long id;
    private String nickname;
    private String scientific_name;
    private String common_name;
    private String received_data_at;
    private String plant_thumb_path;
    private String thumb_path;
    private String thumb_proxy_url; // für den Zugriff mit Token über Proxy}
}
