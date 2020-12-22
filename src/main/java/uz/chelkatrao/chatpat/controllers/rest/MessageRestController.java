package uz.chelkatrao.chatpat.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.chelkatrao.chatpat.models.MessageModel;
import uz.chelkatrao.chatpat.services.MessageService;

@RestController
@RequestMapping("/api/v1/message")
public class MessageRestController {

    private final MessageService messageService;

    @Autowired
    public MessageRestController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/list-by-user")
    public ResponseEntity<?> listOfMessageByUser(@RequestParam Long toUserId,
                                                 @PageableDefault(sort = {"messageTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return messageService.listOfMessageByUser(toUserId, pageable);
    }

    @PostMapping("/save")
    public void saveMessage(@RequestBody MessageModel messageModel) {
        messageService.saveMessage(messageModel);
    }

    @PostMapping("/make-seen-true-by-id")
    public void makeSeenTrueByUserId(@RequestParam Long fromUserId) {
        messageService.makeSeenTrueByUserId(fromUserId);
    }

}
