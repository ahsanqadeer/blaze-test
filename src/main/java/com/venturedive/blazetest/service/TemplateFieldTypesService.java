package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.TemplateFieldTypes;
import com.venturedive.blazetest.repository.TemplateFieldTypesRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TemplateFieldTypes}.
 */
@Service
@Transactional
public class TemplateFieldTypesService {

    private final Logger log = LoggerFactory.getLogger(TemplateFieldTypesService.class);

    private final TemplateFieldTypesRepository templateFieldTypesRepository;

    public TemplateFieldTypesService(TemplateFieldTypesRepository templateFieldTypesRepository) {
        this.templateFieldTypesRepository = templateFieldTypesRepository;
    }

    /**
     * Save a templateFieldTypes.
     *
     * @param templateFieldTypes the entity to save.
     * @return the persisted entity.
     */
    public TemplateFieldTypes save(TemplateFieldTypes templateFieldTypes) {
        log.debug("Request to save TemplateFieldTypes : {}", templateFieldTypes);
        return templateFieldTypesRepository.save(templateFieldTypes);
    }

    /**
     * Update a templateFieldTypes.
     *
     * @param templateFieldTypes the entity to save.
     * @return the persisted entity.
     */
    public TemplateFieldTypes update(TemplateFieldTypes templateFieldTypes) {
        log.debug("Request to update TemplateFieldTypes : {}", templateFieldTypes);
        return templateFieldTypesRepository.save(templateFieldTypes);
    }

    /**
     * Partially update a templateFieldTypes.
     *
     * @param templateFieldTypes the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TemplateFieldTypes> partialUpdate(TemplateFieldTypes templateFieldTypes) {
        log.debug("Request to partially update TemplateFieldTypes : {}", templateFieldTypes);

        return templateFieldTypesRepository
            .findById(templateFieldTypes.getId())
            .map(existingTemplateFieldTypes -> {
                if (templateFieldTypes.getType() != null) {
                    existingTemplateFieldTypes.setType(templateFieldTypes.getType());
                }
                if (templateFieldTypes.getIsList() != null) {
                    existingTemplateFieldTypes.setIsList(templateFieldTypes.getIsList());
                }
                if (templateFieldTypes.getAttachments() != null) {
                    existingTemplateFieldTypes.setAttachments(templateFieldTypes.getAttachments());
                }

                return existingTemplateFieldTypes;
            })
            .map(templateFieldTypesRepository::save);
    }

    /**
     * Get all the templateFieldTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TemplateFieldTypes> findAll(Pageable pageable) {
        log.debug("Request to get all TemplateFieldTypes");
        return templateFieldTypesRepository.findAll(pageable);
    }

    /**
     * Get one templateFieldTypes by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TemplateFieldTypes> findOne(Long id) {
        log.debug("Request to get TemplateFieldTypes : {}", id);
        return templateFieldTypesRepository.findById(id);
    }

    /**
     * Delete the templateFieldTypes by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TemplateFieldTypes : {}", id);
        templateFieldTypesRepository.deleteById(id);
    }
}
