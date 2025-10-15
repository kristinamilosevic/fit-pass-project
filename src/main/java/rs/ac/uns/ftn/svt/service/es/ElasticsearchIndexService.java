package rs.ac.uns.ftn.svt.service.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Service
public class ElasticsearchIndexService {

    private final ElasticsearchClient elasticsearchClient;
    private static final String INDEX_NAME = "facilities";

    public ElasticsearchIndexService(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @PostConstruct
    public void createIndexIfNotExists() {
        try {
            boolean exists = elasticsearchClient.indices()
                    .exists(e -> e.index(INDEX_NAME))
                    .value();

            if (exists) {
                System.out.println("Elasticsearch index '" + INDEX_NAME + "' already exists.");
                return;
            }

            String indexConfigJson = """
            {
              "settings": {
                "analysis": {
                  "filter": {
                    "cyr_to_lat": {
                      "type": "icu_transform",
                      "id": "Cyrillic-Latin"
                    }
                  },
                  "analyzer": {
                    "serbian_custom": {
                      "tokenizer": "standard",
                      "filter": ["lowercase", "cyr_to_lat"]
                    },
                    "autocomplete": {
                      "tokenizer": "edge_ngram_tokenizer",
                      "filter": ["lowercase", "cyr_to_lat"]
                    }
                  },
                  "tokenizer": {
                    "edge_ngram_tokenizer": {
                      "type": "edge_ngram",
                      "min_gram": 1,
                      "max_gram": 20,
                      "token_chars": ["letter", "digit"]
                    }
                  }
                }
              },
              "mappings": {
                "properties": {
                  "name": {
                    "type": "text",
                    "analyzer": "autocomplete",
                    "search_analyzer": "serbian_custom"
                  },
                  "description": {
                    "type": "text",
                    "analyzer": "autocomplete",
                    "search_analyzer": "serbian_custom"
                  },
                  "pdfText": {
                    "type": "text",
                    "analyzer": "autocomplete",
                    "search_analyzer": "serbian_custom"
                  },
                  "city": { "type": "keyword" },
                  "totalRating": { "type": "double" }
                }
              }
            }
            """;

            ByteArrayInputStream jsonStream = new ByteArrayInputStream(indexConfigJson.getBytes(StandardCharsets.UTF_8));

            CreateIndexResponse response = elasticsearchClient.indices()
                    .create(c -> c
                            .index(INDEX_NAME)
                            .withJson(jsonStream)
                    );

            if (response.acknowledged()) {
                System.out.println("Created Elasticsearch index: " + INDEX_NAME);
            } else {
                System.err.println("Index creation not acknowledged for: " + INDEX_NAME);
            }

        } catch (Exception e) {
            System.err.println("Error while creating Elasticsearch index '" + INDEX_NAME + "': " + e.getMessage());
            e.printStackTrace();
        }
    }
}
