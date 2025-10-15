package rs.ac.uns.ftn.svt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthentitacionRequest {

    private String email;
    private String password;
}
