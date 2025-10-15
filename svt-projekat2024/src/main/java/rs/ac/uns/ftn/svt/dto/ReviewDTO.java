package rs.ac.uns.ftn.svt.dto;

import rs.ac.uns.ftn.svt.model.Rate;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class ReviewDTO {
    private Long id;
    private LocalDateTime createdAt;
    private Integer exerciseCount;
    private Boolean hidden;
    private String facilityName;
    private String commentText;
    private Rate rate;

    public ReviewDTO(Long id, LocalDateTime createdAt, Integer exerciseCount, Boolean hidden,
                     String facilityName, String commentText, Rate rate) {
        this.id = id;
        this.createdAt = createdAt;
        this.exerciseCount = exerciseCount;
        this.hidden = hidden;
        this.facilityName = facilityName;
        this.commentText = commentText;
        this.rate = rate;
    }
}
