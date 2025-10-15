package rs.ac.uns.ftn.svt.repository.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import rs.ac.uns.ftn.svt.model.es.FacilityDocument;

import java.util.List;

public interface FacilitySearchRepository extends ElasticsearchRepository<FacilityDocument, Long> {
    List<FacilityDocument> findByNameContainingOrDescriptionContainingOrPdfTextContaining(String name, String description, String pdfText);
}
