package uz.pdp.projects;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Project {
    private Long id;
    private String title;
    private String description;
    private String tasks;
    private String startTime;
    private String deadline;
}
