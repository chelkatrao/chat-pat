package uz.chelkatrao.chatpat.services;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.chelkatrao.chatpat.domains.Messages;
import uz.chelkatrao.chatpat.models.MessageModel;
import uz.chelkatrao.chatpat.repositories.MessageRepository;
import uz.chelkatrao.chatpat.repositories.UserRepository;
import uz.chelkatrao.chatpat.security.SecurityUtils;

import java.util.Date;

@Service
public class MessageService {

    Logger logger = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    public MessageService(MessageRepository messageRepository,
                          UserRepository userRepository,
                          SecurityUtils securityUtils) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.securityUtils = securityUtils;
    }

    /**
     * @param toUserId sending user Id
     * @param pageable pageable model
     * @return Sends 15 messages per request
     */
    @SneakyThrows
    public ResponseEntity<?> listOfMessageByUser(Long toUserId, Pageable pageable) {
        Page<MessageModel> messagePager = messageRepository.getMessageByUser(
                securityUtils.currentUser(),
                userRepository.findById(toUserId).orElseThrow(),
                pageable);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("totalPages",
                String.valueOf(messagePager.getTotalPages()));

        return ResponseEntity.ok().headers(responseHeaders).body(messagePager.toList());
    }

    /**
     * @param messageModel save sending message
     */
    public void saveMessage(MessageModel messageModel) {
        Messages messages = new Messages();
        messages.setMessage(messageModel.getMessage());
        messages.setFrom(securityUtils.currentUser());
        messages.setTo(userRepository.findById(messageModel.getToId()).orElseThrow());
        messages.setMessageTime(new Date());
        messageRepository.save(messages);
        logger.info("messages saved successfully");
    }

    @Transactional
    public void makeSeenTrueByUserId(long fromUserId) {
        messageRepository.makeSeenTrueByUserId(fromUserId, securityUtils.currentUser().getId());
    }
}
