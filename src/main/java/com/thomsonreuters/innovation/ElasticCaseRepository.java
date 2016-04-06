package com.thomsonreuters.innovation;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "cases", path = "elastic-cases")
public interface ElasticCaseRepository extends ElasticsearchRepository<ElasticCase, String> {
}
