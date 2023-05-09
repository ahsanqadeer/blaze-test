package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.Users;
import com.venturedive.blazetest.repository.UsersRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Users}.
 */
@Service
@Transactional
public class UsersService {

    private final Logger log = LoggerFactory.getLogger(UsersService.class);

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    /**
     * Save a users.
     *
     * @param users the entity to save.
     * @return the persisted entity.
     */
    public Users save(Users users) {
        log.debug("Request to save Users : {}", users);
        return usersRepository.save(users);
    }

    /**
     * Update a users.
     *
     * @param users the entity to save.
     * @return the persisted entity.
     */
    public Users update(Users users) {
        log.debug("Request to update Users : {}", users);
        return usersRepository.save(users);
    }

    /**
     * Partially update a users.
     *
     * @param users the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Users> partialUpdate(Users users) {
        log.debug("Request to partially update Users : {}", users);

        return usersRepository
            .findById(users.getId())
            .map(existingUsers -> {
                if (users.getFirstName() != null) {
                    existingUsers.setFirstName(users.getFirstName());
                }
                if (users.getLastName() != null) {
                    existingUsers.setLastName(users.getLastName());
                }
                if (users.getPassword() != null) {
                    existingUsers.setPassword(users.getPassword());
                }
                if (users.getLastActive() != null) {
                    existingUsers.setLastActive(users.getLastActive());
                }
                if (users.getStatus() != null) {
                    existingUsers.setStatus(users.getStatus());
                }
                if (users.getCreatedBy() != null) {
                    existingUsers.setCreatedBy(users.getCreatedBy());
                }
                if (users.getCreatedAt() != null) {
                    existingUsers.setCreatedAt(users.getCreatedAt());
                }
                if (users.getUpdatedBy() != null) {
                    existingUsers.setUpdatedBy(users.getUpdatedBy());
                }
                if (users.getUpdatedAt() != null) {
                    existingUsers.setUpdatedAt(users.getUpdatedAt());
                }
                if (users.getEmail() != null) {
                    existingUsers.setEmail(users.getEmail());
                }
                if (users.getIsDeleted() != null) {
                    existingUsers.setIsDeleted(users.getIsDeleted());
                }
                if (users.getEmailVerified() != null) {
                    existingUsers.setEmailVerified(users.getEmailVerified());
                }
                if (users.getProvider() != null) {
                    existingUsers.setProvider(users.getProvider());
                }
                if (users.getEmailVerificationToken() != null) {
                    existingUsers.setEmailVerificationToken(users.getEmailVerificationToken());
                }
                if (users.getForgotPasswordToken() != null) {
                    existingUsers.setForgotPasswordToken(users.getForgotPasswordToken());
                }

                return existingUsers;
            })
            .map(usersRepository::save);
    }

    /**
     * Get all the users.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Users> findAll(Pageable pageable) {
        log.debug("Request to get all Users");
        return usersRepository.findAll(pageable);
    }

    /**
     * Get all the users with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Users> findAllWithEagerRelationships(Pageable pageable) {
        return usersRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one users by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Users> findOne(Long id) {
        log.debug("Request to get Users : {}", id);
        return usersRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the users by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Users : {}", id);
        usersRepository.deleteById(id);
    }
}
