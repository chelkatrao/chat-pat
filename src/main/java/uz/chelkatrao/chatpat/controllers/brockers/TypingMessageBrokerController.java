package uz.chelkatrao.chatpat.controllers.brockers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import uz.chelkatrao.chatpat.domains.User;
import uz.chelkatrao.chatpat.models.MessageBrokerModel;
import uz.chelkatrao.chatpat.repositories.UserRepository;

import java.security.Principal;
import java.util.Optional;

@Controller
public class TypingMessageBrokerController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserRepository userRepository;

    @Autowired
    public TypingMessageBrokerController(SimpMessagingTemplate simpMessagingTemplate,
                                         UserRepository userRepository) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userRepository = userRepository;
    }

    @MessageMapping("/typing")
    public void sendTypingEvent(MessageBrokerModel msgBrokerModel, Principal principal) {
        Optional<User> userOptional = userRepository.findById(msgBrokerModel.getToId());
        msgBrokerModel.setFromId(userRepository.findByLogin(principal.getName()).getId());
        userOptional.ifPresent(user -> simpMessagingTemplate.convertAndSendToUser(user.getLogin(), "/topic/typing", msgBrokerModel));
    }

}
