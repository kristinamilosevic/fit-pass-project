package rs.ac.uns.ftn.svt.model;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
@DiscriminatorValue("ADMIN")
public class Administrator extends User {
}
