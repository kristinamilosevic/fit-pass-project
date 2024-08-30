package rs.ac.uns.ftn.svt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svt.model.WorkDay;
import rs.ac.uns.ftn.svt.repository.WorkDayRepository;

import java.util.List;

@Service
public class WorkDayService {
    private final WorkDayRepository workDayRepository;

    @Autowired
    public WorkDayService(WorkDayRepository workDayRepository) {
        this.workDayRepository = workDayRepository;
    }

    public List<WorkDay> getWorkDaysByFacilityId(Long facilityId) {
        return workDayRepository.findByFacilityId(facilityId);
    }
}
