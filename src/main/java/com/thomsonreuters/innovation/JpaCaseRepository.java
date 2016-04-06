package com.thomsonreuters.innovation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "cases", path = "jpa-cases")
public interface JpaCaseRepository extends JpaRepository<JpaCase, Long> {
}
