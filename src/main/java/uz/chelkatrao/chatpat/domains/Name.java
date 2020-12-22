package uz.chelkatrao.chatpat.domains;

import lombok.Setter;

import javax.persistence.Embeddable;

@Setter
@Embeddable
public class Name {

    public String first;

    public String middle;

    public String last;

}
