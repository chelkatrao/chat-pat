package uz.chelkatrao.chatpat.domains;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "profile_photo")
public class ProfilePhoto extends BaseDomain {
    private String contentType;
    private String extension;
    private Long fileSize;
    private String hashId;
    private String name;
    private String uploadPath;
}
