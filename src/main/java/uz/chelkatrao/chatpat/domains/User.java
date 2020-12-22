package uz.chelkatrao.chatpat.domains;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Getter
@Setter
@Entity(name = "chat_user")
public class User extends BaseDomain {

    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    private Name name;

    private boolean isActive;

    @OneToOne
    private ProfilePhoto profilePhoto;

}
