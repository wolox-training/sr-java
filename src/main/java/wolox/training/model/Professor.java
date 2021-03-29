package wolox.training.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("professor")
public class Professor extends User {

    @Column
        private String subject;

}
