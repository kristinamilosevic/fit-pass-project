package rs.ac.uns.ftn.svt.model.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Document(indexName = "facilities")
public class FacilityDocument {

    @Id
    private Long id;

    private String name;
    private String description;
    private String pdfText;
    private String city;
    private Double totalRating;

}
