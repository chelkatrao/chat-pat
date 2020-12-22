package uz.chelkatrao.chatpat.domains;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "files")
public class Files extends BaseDomain {
    private String contentType;
    private String extension;
    private String fileSize;
    private String hashId;
    private String name;
    private String uploadPath;
}
