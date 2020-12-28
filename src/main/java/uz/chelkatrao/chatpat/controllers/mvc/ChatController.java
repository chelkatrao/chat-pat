package uz.chelkatrao.chatpat.controllers.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.chelkatrao.chatpat.security.SecurityUtils;

@Controller
@RequestMapping("/chat")
public class ChatController {

    private final SecurityUtils securityUtils;

    @Autowired
    public ChatController(SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
    }

    @GetMapping
    public String chat(Model model) {
        model.addAttribute("userName", securityUtils.currentUser().getName());
        return "chat";
    }

}
