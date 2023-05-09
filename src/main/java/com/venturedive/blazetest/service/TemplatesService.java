package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.Templates;
import com.venturedive.blazetest.repository.TemplatesRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Templates}.
 */
@Service
@Transactional
public class TemplatesService {

    private final Logger log = LoggerFactory.getLogger(TemplatesService.class);

    private final TemplatesRepository templatesRepository;

    public TemplatesService(TemplatesRepository templatesRepository) {
        this.templatesRepository = templatesRepository;
    }

    /**
     * Save a templates.
     *
     * @param templates the entity to save.
     * @return the persisted entity.
     */
    public Templates save(Templates templates) {
        log.debug("Request to save Templates : {}", templates);
        return templatesRepository.save(templates);
    }

    /**
     * Update a templates.
     *
     * @param templates the entity to save.
     * @return the persisted entity.
     */
    public Templates update(Templates templates) {
        log.debug("Request to update Templates : {}", templates);
        return templatesRepository.save(templates);
    }

    /**
     * Partially update a templates.
     *
     * @param templates the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Templates> partialUpdate(Templates templates) {
        log.debug("Request to partially update Templates : {}", templates);

        return templatesRepository
            .findById(templates.getId())
            .map(existingTemplates -> {
                if (templates.getTemplateName() != null) {
                    existingTemplates.setTemplateName(templates.getTemplateName());
                }
                if (templates.getCreatedAt() != null) {
                    existingTemplates.setCreatedAt(templates.getCreatedAt());
                }
                if (templates.getCreatedBy() != null) {
                    existingTemplates.setCreatedBy(templates.getCreatedBy());
                }

                return existingTemplates;
            })
            .map(templatesRepository::save);
    }

    /**
     * Get all the templates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Templates> findAll(Pageable pageable) {
        log.debug("Request to get all Templates");
        return templatesRepository.findAll(pageable);
    }

    /**
     * Get all the templates with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Templates> findAllWithEagerRelationships(Pageable pageable) {
        return templatesRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one templates by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Templates> findOne(Long id) {
        log.debug("Request to get Templates : {}", id);
        return templatesRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the templates by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Templates : {}", id);
        templatesRepository.deleteById(id);
    }
}
