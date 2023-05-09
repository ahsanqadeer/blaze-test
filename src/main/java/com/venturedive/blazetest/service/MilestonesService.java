package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.Milestones;
import com.venturedive.blazetest.repository.MilestonesRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Milestones}.
 */
@Service
@Transactional
public class MilestonesService {

    private final Logger log = LoggerFactory.getLogger(MilestonesService.class);

    private final MilestonesRepository milestonesRepository;

    public MilestonesService(MilestonesRepository milestonesRepository) {
        this.milestonesRepository = milestonesRepository;
    }

    /**
     * Save a milestones.
     *
     * @param milestones the entity to save.
     * @return the persisted entity.
     */
    public Milestones save(Milestones milestones) {
        log.debug("Request to save Milestones : {}", milestones);
        return milestonesRepository.save(milestones);
    }

    /**
     * Update a milestones.
     *
     * @param milestones the entity to save.
     * @return the persisted entity.
     */
    public Milestones update(Milestones milestones) {
        log.debug("Request to update Milestones : {}", milestones);
        return milestonesRepository.save(milestones);
    }

    /**
     * Partially update a milestones.
     *
     * @param milestones the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Milestones> partialUpdate(Milestones milestones) {
        log.debug("Request to partially update Milestones : {}", milestones);

        return milestonesRepository
            .findById(milestones.getId())
            .map(existingMilestones -> {
                if (milestones.getName() != null) {
                    existingMilestones.setName(milestones.getName());
                }
                if (milestones.getDescription() != null) {
                    existingMilestones.setDescription(milestones.getDescription());
                }
                if (milestones.getReference() != null) {
                    existingMilestones.setReference(milestones.getReference());
                }
                if (milestones.getStartDate() != null) {
                    existingMilestones.setStartDate(milestones.getStartDate());
                }
                if (milestones.getEndDate() != null) {
                    existingMilestones.setEndDate(milestones.getEndDate());
                }
                if (milestones.getIsCompleted() != null) {
                    existingMilestones.setIsCompleted(milestones.getIsCompleted());
                }

                return existingMilestones;
            })
            .map(milestonesRepository::save);
    }

    /**
     * Get all the milestones.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Milestones> findAll(Pageable pageable) {
        log.debug("Request to get all Milestones");
        return milestonesRepository.findAll(pageable);
    }

    /**
     * Get one milestones by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Milestones> findOne(Long id) {
        log.debug("Request to get Milestones : {}", id);
        return milestonesRepository.findById(id);
    }

    /**
     * Delete the milestones by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Milestones : {}", id);
        milestonesRepository.deleteById(id);
    }
}
