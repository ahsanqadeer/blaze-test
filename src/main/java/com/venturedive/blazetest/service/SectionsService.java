package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.Sections;
import com.venturedive.blazetest.repository.SectionsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Sections}.
 */
@Service
@Transactional
public class SectionsService {

    private final Logger log = LoggerFactory.getLogger(SectionsService.class);

    private final SectionsRepository sectionsRepository;

    public SectionsService(SectionsRepository sectionsRepository) {
        this.sectionsRepository = sectionsRepository;
    }

    /**
     * Save a sections.
     *
     * @param sections the entity to save.
     * @return the persisted entity.
     */
    public Sections save(Sections sections) {
        log.debug("Request to save Sections : {}", sections);
        return sectionsRepository.save(sections);
    }

    /**
     * Update a sections.
     *
     * @param sections the entity to save.
     * @return the persisted entity.
     */
    public Sections update(Sections sections) {
        log.debug("Request to update Sections : {}", sections);
        return sectionsRepository.save(sections);
    }

    /**
     * Partially update a sections.
     *
     * @param sections the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Sections> partialUpdate(Sections sections) {
        log.debug("Request to partially update Sections : {}", sections);

        return sectionsRepository
            .findById(sections.getId())
            .map(existingSections -> {
                if (sections.getName() != null) {
                    existingSections.setName(sections.getName());
                }
                if (sections.getDescription() != null) {
                    existingSections.setDescription(sections.getDescription());
                }
                if (sections.getCreatedAt() != null) {
                    existingSections.setCreatedAt(sections.getCreatedAt());
                }
                if (sections.getCreatedBy() != null) {
                    existingSections.setCreatedBy(sections.getCreatedBy());
                }
                if (sections.getUpdatedAt() != null) {
                    existingSections.setUpdatedAt(sections.getUpdatedAt());
                }
                if (sections.getUpdatedBy() != null) {
                    existingSections.setUpdatedBy(sections.getUpdatedBy());
                }

                return existingSections;
            })
            .map(sectionsRepository::save);
    }

    /**
     * Get all the sections.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Sections> findAll(Pageable pageable) {
        log.debug("Request to get all Sections");
        return sectionsRepository.findAll(pageable);
    }

    /**
     * Get one sections by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Sections> findOne(Long id) {
        log.debug("Request to get Sections : {}", id);
        return sectionsRepository.findById(id);
    }

    /**
     * Delete the sections by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Sections : {}", id);
        sectionsRepository.deleteById(id);
    }
}
