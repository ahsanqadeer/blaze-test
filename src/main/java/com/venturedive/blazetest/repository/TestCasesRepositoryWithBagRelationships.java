package com.venturedive.blazetest.repository;

import com.venturedive.blazetest.domain.TestCases;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface TestCasesRepositoryWithBagRelationships {
    Optional<TestCases> fetchBagRelationships(Optional<TestCases> testCases);

    List<TestCases> fetchBagRelationships(List<TestCases> testCases);

    Page<TestCases> fetchBagRelationships(Page<TestCases> testCases);
}
