package uz.pdp.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TaskDto {
    private String taskId;
    private String taskName;
    private String description;
}
