package uz.chelkatrao.chatpat.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class MessageModel {
    private Long messageId;
    private String message;
    private Date messageTime;
    private boolean seen;
    private Long fromId;
    private Long toId;
    private boolean ownMessage;
}
