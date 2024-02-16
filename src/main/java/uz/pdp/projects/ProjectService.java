package uz.pdp.projects;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.config.Task;
import uz.pdp.dto.ProjectViewDto;
import uz.pdp.dto.TaskDto;
import uz.pdp.user.User;
import uz.pdp.user.UserService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectService {
    public static List<ProjectViewDto> getProjects(JdbcTemplate jdbcTemplate) {
        List<Project> projects = jdbcTemplate.query("SELECT * FROM projects", new ProjectRowMapper());
        List<ProjectViewDto> dtos = projects.stream().map(p -> ProjectViewDto.builder()
                .id(p.getId())
                .title(p.getTitle())
                .startTime(p.getStartTime())
                .description(p.getDescription())
                .deadline(p.getDeadline())
                .build()).toList();

        dtos.forEach(p -> p.setDevelopers(
                jdbcTemplate.query("SELECT * FROM users  WHERE project_id = ?", new UserService.UserRowMapper(), p.getId())
        ));
        dtos.forEach(p -> p.setTasks(
                jdbcTemplate.query("SELECT * FROM tasks  WHERE project_id = ?", new TaskRowMapper(), p.getId())
        ));
        return dtos;
    }



    private static class TaskRowMapper implements RowMapper<TaskDto> {

        @Override
        public TaskDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return TaskDto.builder()
                    .taskId(String.valueOf(rs.getLong("task_id")))
                    .taskName(rs.getString("task_name"))
                    .description(rs.getString("description"))
                    .build();
        }
    }

    private static class ProjectRowMapper implements RowMapper<Project> {

        @Override
        public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Project.builder()
                    .id(rs.getLong("project_id"))
                    .title(rs.getString("title"))
                    .startTime(String.valueOf(rs.getDate("start_time")))
                    .description(rs.getString("description"))
                    .deadline(String.valueOf(rs.getDate("deadline")))
                    .build();
        }
    }
}


