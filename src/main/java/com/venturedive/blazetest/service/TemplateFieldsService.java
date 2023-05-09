package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.TemplateFields;
import com.venturedive.blazetest.repository.TemplateFieldsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TemplateFields}.
 */
@Service
@Transactional
public class TemplateFieldsService {

    private final Logger log = LoggerFactory.getLogger(TemplateFieldsService.class);

    private final TemplateFieldsRepository templateFieldsRepository;

    public TemplateFieldsService(TemplateFieldsRepository templateFieldsRepository) {
        this.templateFieldsRepository = templateFieldsRepository;
    }

    /**
     * Save a templateFields.
     *
     * @param templateFields the entity to save.
     * @return the persisted entity.
     */
    public TemplateFields save(TemplateFields templateFields) {
        log.debug("Request to save TemplateFields : {}", templateFields);
        return templateFieldsRepository.save(templateFields);
    }

    /**
     * Update a templateFields.
     *
     * @param templateFields the entity to save.
     * @return the persisted entity.
     */
    public TemplateFields update(TemplateFields templateFields) {
        log.debug("Request to update TemplateFields : {}", templateFields);
        return templateFieldsRepository.save(templateFields);
    }

    /**
     * Partially update a templateFields.
     *
     * @param templateFields the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TemplateFields> partialUpdate(TemplateFields templateFields) {
        log.debug("Request to partially update TemplateFields : {}", templateFields);

        return templateFieldsRepository
            .findById(templateFields.getId())
            .map(existingTemplateFields -> {
                if (templateFields.getFieldName() != null) {
                    existingTemplateFields.setFieldName(templateFields.getFieldName());
                }

                return existingTemplateFields;
            })
            .map(templateFieldsRepository::save);
    }

    /**
     * Get all the templateFields.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TemplateFields> findAll(Pageable pageable) {
        log.debug("Request to get all TemplateFields");
        return templateFieldsRepository.findAll(pageable);
    }

    /**
     * Get all the templateFields with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TemplateFields> findAllWithEagerRelationships(Pageable pageable) {
        return templateFieldsRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one templateFields by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TemplateFields> findOne(Long id) {
        log.debug("Request to get TemplateFields : {}", id);
        return templateFieldsRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the templateFields by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TemplateFields : {}", id);
        templateFieldsRepository.deleteById(id);
    }
}
