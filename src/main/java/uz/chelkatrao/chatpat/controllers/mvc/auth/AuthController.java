package uz.chelkatrao.chatpat.controllers.mvc.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.chelkatrao.chatpat.models.UserFormModel;
import uz.chelkatrao.chatpat.services.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Controller
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping({"/login", "/"})
    public String loginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        String errorMessage = "";
        String successLogOut = "";
        if (error != null) {
            errorMessage = "Username or Password is incorrect !!";
        }
        if (logout != null) {
            successLogOut = "You have been successfully logged out !!";
        }
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("successLogOut", successLogOut);
        model.addAttribute("userForm", new UserFormModel());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("userForm") UserFormModel userForm) {
        return authService.login(userForm);
    }

    @GetMapping("/logout")
    public String logOutPage(HttpServletRequest request, HttpServletResponse response) {
        authService.logOut(request, response);
        return "redirect:/login?logout=true";
    }

    @GetMapping("/registration")
    public String registration(Model model,
                               @RequestParam(value = "error", required = false) String error) {
        model.addAttribute("userForm", new UserFormModel());
        model.addAttribute("errorMessage", Objects.requireNonNullElse(error, ""));
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("registerModel") UserFormModel userFormModel) {
        return authService.registration(userFormModel);
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "errors/403";
    }

}
