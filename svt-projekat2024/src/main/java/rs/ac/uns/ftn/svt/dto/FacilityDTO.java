package rs.ac.uns.ftn.svt.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityDTO {
    private Long id;
    private String name;
    private String address;
    private String city;
    private LocalDateTime createdAt;
    private String description;

    // Lista radnih dana
    private List<WorkDayDTO> workDays;

    // Lista disciplina
    private List<DisciplineDTO> disciplines;

    public String getCreatedAt() {
        if (createdAt != null) {
            return createdAt.format(DateTimeFormatter.ISO_DATE_TIME);
        }
        return null;
    }

    // WorkDay DTO
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkDayDTO {
        private LocalDate validFrom;
        private DayOfWeek day;
        private LocalTime fromTime;
        private LocalTime untilTime;
    }

    // Discipline DTO
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DisciplineDTO {
        private String name;
    }
}
