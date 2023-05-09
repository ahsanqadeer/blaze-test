package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.Companies;
import com.venturedive.blazetest.repository.CompaniesRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Companies}.
 */
@Service
@Transactional
public class CompaniesService {

    private final Logger log = LoggerFactory.getLogger(CompaniesService.class);

    private final CompaniesRepository companiesRepository;

    public CompaniesService(CompaniesRepository companiesRepository) {
        this.companiesRepository = companiesRepository;
    }

    /**
     * Save a companies.
     *
     * @param companies the entity to save.
     * @return the persisted entity.
     */
    public Companies save(Companies companies) {
        log.debug("Request to save Companies : {}", companies);
        return companiesRepository.save(companies);
    }

    /**
     * Update a companies.
     *
     * @param companies the entity to save.
     * @return the persisted entity.
     */
    public Companies update(Companies companies) {
        log.debug("Request to update Companies : {}", companies);
        return companiesRepository.save(companies);
    }

    /**
     * Partially update a companies.
     *
     * @param companies the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Companies> partialUpdate(Companies companies) {
        log.debug("Request to partially update Companies : {}", companies);

        return companiesRepository
            .findById(companies.getId())
            .map(existingCompanies -> {
                if (companies.getCountry() != null) {
                    existingCompanies.setCountry(companies.getCountry());
                }
                if (companies.getUrl() != null) {
                    existingCompanies.setUrl(companies.getUrl());
                }
                if (companies.getName() != null) {
                    existingCompanies.setName(companies.getName());
                }
                if (companies.getExpectedNoOfUsers() != null) {
                    existingCompanies.setExpectedNoOfUsers(companies.getExpectedNoOfUsers());
                }
                if (companies.getCreatedBy() != null) {
                    existingCompanies.setCreatedBy(companies.getCreatedBy());
                }
                if (companies.getCreatedAt() != null) {
                    existingCompanies.setCreatedAt(companies.getCreatedAt());
                }
                if (companies.getUpdatedBy() != null) {
                    existingCompanies.setUpdatedBy(companies.getUpdatedBy());
                }
                if (companies.getUpdatedAt() != null) {
                    existingCompanies.setUpdatedAt(companies.getUpdatedAt());
                }

                return existingCompanies;
            })
            .map(companiesRepository::save);
    }

    /**
     * Get all the companies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Companies> findAll(Pageable pageable) {
        log.debug("Request to get all Companies");
        return companiesRepository.findAll(pageable);
    }

    /**
     * Get one companies by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Companies> findOne(Long id) {
        log.debug("Request to get Companies : {}", id);
        return companiesRepository.findById(id);
    }

    /**
     * Delete the companies by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Companies : {}", id);
        companiesRepository.deleteById(id);
    }
}
