package uz.chelkatrao.chatpat.domains;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Messages extends BaseDomain {

    @Column(columnDefinition = "text")
    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    @ColumnDefault("now()")
    private Date messageTime;

    @ManyToOne
    @JoinColumn(name = "from_id")
    private User from;

    @ManyToOne
    @JoinColumn(name = "to_id")
    private User to;

    private boolean seen;

}
