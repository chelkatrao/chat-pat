package uz.chelkatrao.chatpat.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import uz.chelkatrao.chatpat.domains.Name;
import uz.chelkatrao.chatpat.domains.User;
import uz.chelkatrao.chatpat.models.UserFormModel;
import uz.chelkatrao.chatpat.repositories.UserRepository;
import uz.chelkatrao.chatpat.security.CustomAuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class AuthService {

    Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder,
                       UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    /**
     * @param userFormModel password and username fields has in model
     */
    public String login(UserFormModel userFormModel) {
        CustomAuthenticationToken customAuthenticationToken =
                new CustomAuthenticationToken(userFormModel.getLogin(), userFormModel.getPassword());
        try {
            authenticationManager.authenticate(customAuthenticationToken);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "redirect:/login?error=true";
        }
        SecurityContextHolder.getContext().setAuthentication(customAuthenticationToken);
        logger.info("Successfully Login");
        return "redirect:/chat";
    }

    public void logOut(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            logger.error("error while logout");
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        logger.info("Successfully Login Out");
    }

    public String registration(UserFormModel userFormModel) {
        String errorMessage = "";
        String redirectionUrl = "redirect:/registration?error=";

        if (userFormModel.getLogin().isEmpty() && userFormModel.getPassword().isEmpty()) {
            errorMessage = " Login and password can not be empty";
            return redirectionUrl + errorMessage;
        }

        if (userFormModel.getLogin().isEmpty()) {
            errorMessage = " Login can not be empty";
            return redirectionUrl + errorMessage;
        }
        if (userFormModel.getPassword().isEmpty()) {
            errorMessage = " Password can not be empty";
            return redirectionUrl + errorMessage;
        }

        User user = new User();
        user.setLogin(userFormModel.getLogin());
        user.setPassword(passwordEncoder.encode(userFormModel.getPassword()));
        if (!userFormModel.getUsername().isEmpty()) {
            Name name = new Name();
            name.setFirst(userFormModel.getUsername());
            user.setName(name);
        }
        try {
            userRepository.save(user);
        }catch (RuntimeException e){
            errorMessage = "Error while registration";
            return redirectionUrl + errorMessage;
        }

        return login(userFormModel);
    }
}
