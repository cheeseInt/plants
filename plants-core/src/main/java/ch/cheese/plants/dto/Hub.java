package ch.cheese.plants.dto;

import lombok.Data;

@Data
public class Hub {
    private Integer id;
    private String hub_id;
    private String hub_name;
    private String version;
    private Integer status;
    private String received_data_at;
    private String reached_hub_at;
}
