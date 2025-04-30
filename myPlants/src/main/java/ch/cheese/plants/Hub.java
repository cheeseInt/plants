package ch.cheese.plants;

import lombok.Data;

@Data
public class Hub {
    private int id;
    private String hub_id;
    private String hub_name;
    private String version;
    private int status;
    private String received_data_at;
    private String reached_hub_at;
}
