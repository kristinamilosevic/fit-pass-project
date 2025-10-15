package rs.ac.uns.ftn.svt.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer equipment;

    private Integer staff;

    private Integer hygiene;

    private Integer space;

    @OneToMany(mappedBy = "rate")
    private List<Review> reviews;

}
