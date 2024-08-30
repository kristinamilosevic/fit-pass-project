package rs.ac.uns.ftn.svt.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    private String email;
    private String name;
    private String surname;
    private LocalDate birthday;
    private String phoneNumber;
    private String address;
    private String city;
    private String zipCode;
}
