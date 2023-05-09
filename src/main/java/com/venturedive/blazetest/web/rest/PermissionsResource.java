package com.venturedive.blazetest.web.rest;

import com.venturedive.blazetest.domain.Permissions;
import com.venturedive.blazetest.repository.PermissionsRepository;
import com.venturedive.blazetest.service.PermissionsQueryService;
import com.venturedive.blazetest.service.PermissionsService;
import com.venturedive.blazetest.service.criteria.PermissionsCriteria;
import com.venturedive.blazetest.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.venturedive.blazetest.domain.Permissions}.
 */
@RestController
@RequestMapping("/api")
public class PermissionsResource {

    private final Logger log = LoggerFactory.getLogger(PermissionsResource.class);

    private static final String ENTITY_NAME = "permissions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PermissionsService permissionsService;

    private final PermissionsRepository permissionsRepository;

    private final PermissionsQueryService permissionsQueryService;

    public PermissionsResource(
        PermissionsService permissionsService,
        PermissionsRepository permissionsRepository,
        PermissionsQueryService permissionsQueryService
    ) {
        this.permissionsService = permissionsService;
        this.permissionsRepository = permissionsRepository;
        this.permissionsQueryService = permissionsQueryService;
    }

    /**
     * {@code POST  /permissions} : Create a new permissions.
     *
     * @param permissions the permissions to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new permissions, or with status {@code 400 (Bad Request)} if the permissions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/permissions")
    public ResponseEntity<Permissions> createPermissions(@Valid @RequestBody Permissions permissions) throws URISyntaxException {
        log.debug("REST request to save Permissions : {}", permissions);
        if (permissions.getId() != null) {
            throw new BadRequestAlertException("A new permissions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Permissions result = permissionsService.save(permissions);
        return ResponseEntity
            .created(new URI("/api/permissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /permissions/:id} : Updates an existing permissions.
     *
     * @param id the id of the permissions to save.
     * @param permissions the permissions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permissions,
     * or with status {@code 400 (Bad Request)} if the permissions is not valid,
     * or with status {@code 500 (Internal Server Error)} if the permissions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/permissions/{id}")
    public ResponseEntity<Permissions> updatePermissions(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Permissions permissions
    ) throws URISyntaxException {
        log.debug("REST request to update Permissions : {}, {}", id, permissions);
        if (permissions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permissions.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!permissionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Permissions result = permissionsService.update(permissions);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, permissions.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /permissions/:id} : Partial updates given fields of an existing permissions, field will ignore if it is null
     *
     * @param id the id of the permissions to save.
     * @param permissions the permissions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permissions,
     * or with status {@code 400 (Bad Request)} if the permissions is not valid,
     * or with status {@code 404 (Not Found)} if the permissions is not found,
     * or with status {@code 500 (Internal Server Error)} if the permissions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/permissions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Permissions> partialUpdatePermissions(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Permissions permissions
    ) throws URISyntaxException {
        log.debug("REST request to partial update Permissions partially : {}, {}", id, permissions);
        if (permissions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permissions.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!permissionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Permissions> result = permissionsService.partialUpdate(permissions);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, permissions.getId().toString())
        );
    }

    /**
     * {@code GET  /permissions} : get all the permissions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of permissions in body.
     */
    @GetMapping("/permissions")
    public ResponseEntity<List<Permissions>> getAllPermissions(
        PermissionsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Permissions by criteria: {}", criteria);
        Page<Permissions> page = permissionsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /permissions/count} : count all the permissions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/permissions/count")
    public ResponseEntity<Long> countPermissions(PermissionsCriteria criteria) {
        log.debug("REST request to count Permissions by criteria: {}", criteria);
        return ResponseEntity.ok().body(permissionsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /permissions/:id} : get the "id" permissions.
     *
     * @param id the id of the permissions to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the permissions, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/permissions/{id}")
    public ResponseEntity<Permissions> getPermissions(@PathVariable Long id) {
        log.debug("REST request to get Permissions : {}", id);
        Optional<Permissions> permissions = permissionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(permissions);
    }

    /**
     * {@code DELETE  /permissions/:id} : delete the "id" permissions.
     *
     * @param id the id of the permissions to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/permissions/{id}")
    public ResponseEntity<Void> deletePermissions(@PathVariable Long id) {
        log.debug("REST request to delete Permissions : {}", id);
        permissionsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
