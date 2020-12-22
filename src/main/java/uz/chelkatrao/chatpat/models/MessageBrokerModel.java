package uz.chelkatrao.chatpat.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageBrokerModel {
    private String message;
    private Long toId;
    private Long fromId;
}
