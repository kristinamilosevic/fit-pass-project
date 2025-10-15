package rs.ac.uns.ftn.svt.service.es;

import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svt.model.Facility;
import rs.ac.uns.ftn.svt.model.es.FacilityDocument;
import rs.ac.uns.ftn.svt.repository.es.FacilitySearchRepository;
import rs.ac.uns.ftn.svt.service.FacilityService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacilityESService {

    private final FacilitySearchRepository repository;
    private final FacilityService facilityService;
    private final ElasticsearchClient elasticsearchClient;

    public void reindexAllFacilities() {
        repository.deleteAll();

        List<FacilityDocument> documents = facilityService.getAllFacilities()
                .stream()
                .map(this::convertToDocument)
                .collect(Collectors.toList());

        repository.saveAll(documents);
    }

    public void indexFacility(Facility facility) {
        FacilityDocument doc = convertToDocument(facility);
        repository.save(doc);
    }

    public void deleteFacilityFromIndex(Long facilityId) {
        repository.deleteById(facilityId);
    }

    public List<FacilityDocument> searchByNameOrDescription(String nameQuery, String descriptionQuery) {
        if ((nameQuery == null || nameQuery.isEmpty()) && (descriptionQuery == null || descriptionQuery.isEmpty())) {
            return Collections.emptyList();
        }

        try {
            String processedName = nameQuery != null ? toLatin(nameQuery) : null;
            String processedDescription = descriptionQuery != null ? toLatin(descriptionQuery) : null;

            String combinedQuery;
            if (processedName != null && processedDescription != null) {
                combinedQuery = processedName + " " + processedDescription;
            } else if (processedName != null) {
                combinedQuery = processedName;
            } else {
                combinedQuery = processedDescription;
            }

            SearchResponse<FacilityDocument> response = elasticsearchClient.search(s -> s
                            .index("facilities")
                            .query(q -> q
                                    .multiMatch(mm -> mm
                                            .query(combinedQuery)
                                            .fields("name", "description")
                                            .type(TextQueryType.BoolPrefix)
                                    )
                            )
                            .size(50),
                    FacilityDocument.class
            );

            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    private FacilityDocument convertToDocument(Facility facility) {
        FacilityDocument doc = new FacilityDocument();
        doc.setId(facility.getId());
        doc.setName(facility.getName());
        doc.setDescription(facility.getDescription());
        doc.setCity(facility.getCity());
        doc.setTotalRating(facility.getTotalRating());
        return doc;
    }

    private String toLatin(String text) {
        if (text == null) return null;
        char[] cyr = {'А','Б','В','Г','Д','Ђ','Е','Ж','З','И','Ј','К','Л','Љ','М','Н','Њ','О','П','Р','С','Т','Ћ','У','Ф','Х','Ц','Ч','Џ','Ш',
                'а','б','в','г','д','ђ','е','ж','з','и','ј','к','л','љ','м','н','њ','о','п','р','с','т','ћ','у','ф','х','ц','ч','џ','ш'};
        String[] lat = {"A","B","V","G","D","Đ","E","Ž","Z","I","J","K","L","Lj","M","N","Nj","O","P","R","S","T","Ć","U","F","H","C","Č","Dž","Š",
                "a","b","v","g","d","đ","e","ž","z","i","j","k","l","lj","m","n","nj","o","p","r","s","t","ć","u","f","h","c","č","dž","š"};
        for (int i = 0; i < cyr.length; i++) {
            text = text.replace(cyr[i], lat[i].charAt(0));
        }
        return text.toLowerCase();
    }
}
