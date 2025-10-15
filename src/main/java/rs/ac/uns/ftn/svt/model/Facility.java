package rs.ac.uns.ftn.svt.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@Entity
@JsonIgnoreProperties({"reviews", "exercises", "manages", "images", "disciplines", "workDays"})
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private LocalDate createdAt;

    private String address;

    private String city;

    private Double totalRating;

    private boolean active;

    @OneToMany(mappedBy = "facility")
    private List<Review> reviews;

    @OneToMany(mappedBy = "facility")
    private List<Exercise> exercises;

    @OneToMany(mappedBy = "facility")
    private List<Manages> manages;

    @OneToMany(mappedBy = "facility")

    private List<Image> images;

    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Discipline> disciplines = new ArrayList<>();

    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkDay> workDays = new ArrayList<>();

}
