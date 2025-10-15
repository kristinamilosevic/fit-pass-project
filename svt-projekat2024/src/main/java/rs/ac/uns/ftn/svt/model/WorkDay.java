package rs.ac.uns.ftn.svt.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;

@Data
@Entity
public class WorkDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate validFrom;

    @Enumerated(EnumType.STRING)
    private DayOfWeek day;

    private LocalTime fromTime;

    private LocalTime untilTime;

    @ManyToOne
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

    public WorkDay() {
    }

    public WorkDay(Long id, LocalDate validFrom, DayOfWeek day, LocalTime fromTime, LocalTime untilTime, Facility facility) {
        this.id = id;
        this.validFrom = validFrom;
        this.day = day;
        this.fromTime = fromTime;
        this.untilTime = untilTime;
        this.facility = facility;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public LocalTime getFromTime() {
        return fromTime;
    }

    public void setFromTime(LocalTime fromTime) {
        this.fromTime = fromTime;
    }

    public LocalTime getUntilTime() {
        return untilTime;
    }

    public void setUntilTime(LocalTime untilTime) {
        this.untilTime = untilTime;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }
}
