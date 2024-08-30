package rs.ac.uns.ftn.svt.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import jakarta.validation.constraints.Email;

@Data
@Entity
public class AccountRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Email should be valid")
    private String email;

    private LocalDate createdAt;

    private String address;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private String rejectionReason;
}
