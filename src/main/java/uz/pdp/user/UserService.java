package uz.pdp.user;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import uz.pdp.projects.ProjectService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;


public class UserService {

    public static String addUser(String firstname, String lastname, String email, String password, JdbcTemplate jdbcTemplate) {
        Integer count = jdbcTemplate.queryForObject("select count(*) from users where email=?", Integer.class, email);
        if (count > 0) {
            throw new RuntimeException("Email already exists");
        } else {
            jdbcTemplate.update("insert into users(first_name, last_name, email, password) VALUES (?,?,?,?)", firstname, lastname, email, password);
            return getFullNameByEmail(email, jdbcTemplate);
        }
    }

    public static String getFullNameByEmail(String email, JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.queryForObject("select first_name||' '||last_name from users where email=?", String.class, email);
    }

    public static String checkForLogin(String email, String password, JdbcTemplate jdbcTemplate) {
        Integer count = jdbcTemplate.queryForObject("select count(*) from users where email=?", Integer.class, email);
        if (count > 0) {
            String userPassword = jdbcTemplate.queryForObject("select password from users where email=? ", String.class, email);
            if (Objects.equals(userPassword, password)) {
                return getFullNameByEmail(email, jdbcTemplate);
            }
        }
                throw new RuntimeException("Incorrect email or password");
}

    public static class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return User.builder()
                    .userId(rs.getLong("user_id"))
                    .email(rs.getString("email"))
                    .firstname(rs.getString("first_name"))
                    .lastname(rs.getString("last_name"))
                    .projectId(rs.getLong("project_id"))
                    .build();
        }
    }

    public static List<User> getUserList(JdbcTemplate jdbcTemplate){
        return jdbcTemplate.query("SELECT * FROM users WHERE project_id is null", new UserRowMapper());
    }

    public static void addUserToProject(JdbcTemplate jdbcTemplate, Long userId, Long projectId){
         jdbcTemplate.update("update users set project_id=? where user_id=?",projectId,userId);
    }
}
