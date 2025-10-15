package rs.ac.uns.ftn.svt.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Discipline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

}

