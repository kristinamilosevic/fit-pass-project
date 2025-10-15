package rs.ac.uns.ftn.svt.dto;

import lombok.Data;

@Data
public class ManageDTO {
    private Long userId;
    private Long facilityId;
    private String startDate;
    private String endDate;
    private boolean activateFacility;
}
