package uz.chelkatrao.chatpat.security;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uz.chelkatrao.chatpat.domains.ProfilePhoto;
import uz.chelkatrao.chatpat.domains.User;
import uz.chelkatrao.chatpat.repositories.UserRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class SecurityUtils {

    private final UserRepository userRepository;

    @Autowired
    public SecurityUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        if (authentication != null) {
            user = userRepository.findByLogin(((CustomUserDetails) authentication.getPrincipal()).getLogin());
        }
        return user;
    }

}
