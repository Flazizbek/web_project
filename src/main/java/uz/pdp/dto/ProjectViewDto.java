package uz.pdp.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.pdp.user.User;

import java.util.List;

@Getter
@Setter
@Builder
public class ProjectViewDto {
    private Long id;
    private String title;
    private String description;
    private List<User> developers;
    private List<TaskDto> tasks;
    private String startTime;
    private String deadline;
}
