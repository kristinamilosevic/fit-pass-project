package rs.ac.uns.ftn.svt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svt.model.es.FacilityDocument;
import rs.ac.uns.ftn.svt.repository.es.FacilitySearchRepository;

@Service
@RequiredArgsConstructor
public class TestEsService implements CommandLineRunner {

    private final FacilitySearchRepository repository;

    @Override
    public void run(String... args) throws Exception {
        FacilityDocument doc = new FacilityDocument();
        doc.setId(1L);
        doc.setName("FitGym");
        doc.setDescription("Gym with cardio and weight training");
        doc.setPdfText("This PDF text will be indexed in Elasticsearch.");
        doc.setCity("Belgrade");
        doc.setTotalRating(4.5);

        repository.save(doc);

        System.out.println("Facility saved to Elasticsearch!");
    }
}
