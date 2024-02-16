package uz.pdp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.projects.ProjectService;
import uz.pdp.user.UserService;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public ModelAndView signup(ModelAndView modelAndView,
                               @RequestParam("firstname") String firstname,
                               @RequestParam("lastname") String lastname,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password) {
        String ans = UserService.addUser(firstname, lastname, email, password, jdbcTemplate);
        modelAndView.addObject("ans", ans);
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public ModelAndView login(ModelAndView modelAndView,
                              @RequestParam("email") String email,
                              @RequestParam("password") String password) {
        String ans = UserService.checkForLogin(email, password, jdbcTemplate);
        modelAndView.addObject("ans", ans);
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @GetMapping("/projects")
    public ModelAndView projects(ModelAndView modelAndView) {
        modelAndView.addObject("projects", ProjectService.getProjects(jdbcTemplate));
        modelAndView.setViewName("projects");
        return modelAndView;
    }

    @GetMapping("/add_project")
    public String addProject() {
        return "add_project";
    }

    @PostMapping("/add_project")
    public ModelAndView addProject(ModelAndView modelAndView, @RequestParam("title") String title, @RequestParam("description") String description, @RequestParam("deadline") String deadline) {
        jdbcTemplate.update("insert into projects(title, description, start_time, deadline)" +
                "values (?,?,now(),now())", title, description);
        modelAndView.setViewName("/projects");
        return modelAndView;
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/add_developer/{id}")
    public ModelAndView addDeveloper(ModelAndView modelAndView, @PathVariable("id") Long id) {
        String title = jdbcTemplate.queryForObject("SELECT title FROM projects WHERE project_id=?", String.class, id);
        modelAndView.addObject("title", title);
        modelAndView.addObject("users", UserService.getUserList(jdbcTemplate));
        modelAndView.setViewName("add_developer");
        return modelAndView;
    }

    @PostMapping("/add_developer/{id}")
    public ModelAndView showDeveloper(ModelAndView modelAndView, @PathVariable("id") Long id, @RequestParam("id") Long userId) {
        UserService.addUserToProject(jdbcTemplate, userId, id);
        String title = jdbcTemplate.queryForObject("SELECT title FROM projects WHERE project_id=?", String.class, id);
        modelAndView.addObject("projectId", id);
        modelAndView.addObject("title", title);
        modelAndView.addObject("users", UserService.getUserList(jdbcTemplate));
        modelAndView.setViewName("add_developer");
        return modelAndView;
    }

    @GetMapping("/project/{projectId}/add/developer")
    public ModelAndView addDeveloper(ModelAndView modelAndView, @PathVariable("projectId") Long id, @RequestParam("id") Long userId) {
        UserService.addUserToProject(jdbcTemplate, userId, id);
        String title = jdbcTemplate.queryForObject("SELECT title FROM projects WHERE project_id=?", String.class, id);
        modelAndView.addObject("projectId", id);
        modelAndView.addObject("title", title);
        modelAndView.addObject("users", UserService.getUserList(jdbcTemplate));
        modelAndView.setViewName("add_developer");
        return modelAndView;
    }


}


