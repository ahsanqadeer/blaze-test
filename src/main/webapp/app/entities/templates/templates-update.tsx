import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICompanies } from 'app/shared/model/companies.model';
import { getEntities as getCompanies } from 'app/entities/companies/companies.reducer';
import { ITemplateFields } from 'app/shared/model/template-fields.model';
import { getEntities as getTemplateFields } from 'app/entities/template-fields/template-fields.reducer';
import { ITemplates } from 'app/shared/model/templates.model';
import { getEntity, updateEntity, createEntity, reset } from './templates.reducer';

export const TemplatesUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const companies = useAppSelector(state => state.companies.entities);
  const templateFields = useAppSelector(state => state.templateFields.entities);
  const templatesEntity = useAppSelector(state => state.templates.entity);
  const loading = useAppSelector(state => state.templates.loading);
  const updating = useAppSelector(state => state.templates.updating);
  const updateSuccess = useAppSelector(state => state.templates.updateSuccess);

  const handleClose = () => {
    navigate('/templates' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCompanies({}));
    dispatch(getTemplateFields({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createdAt = convertDateTimeToServer(values.createdAt);

    const entity = {
      ...templatesEntity,
      ...values,
      templateFields: mapIdList(values.templateFields),
      company: companies.find(it => it.id.toString() === values.company.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdAt: displayDefaultDateTime(),
        }
      : {
          ...templatesEntity,
          createdAt: convertDateTimeFromServer(templatesEntity.createdAt),
          company: templatesEntity?.company?.id,
          templateFields: templatesEntity?.templateFields?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="blazeTestApp.templates.home.createOrEditLabel" data-cy="TemplatesCreateUpdateHeading">
            Create or edit a Templates
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="templates-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Template Name"
                id="templates-templateName"
                name="templateName"
                data-cy="templateName"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField
                label="Created At"
                id="templates-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Created By" id="templates-createdBy" name="createdBy" data-cy="createdBy" type="text" />
              <ValidatedField id="templates-company" name="company" data-cy="company" label="Company" type="select" required>
                <option value="" key="0" />
                {companies
                  ? companies.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <ValidatedField
                label="Template Field"
                id="templates-templateField"
                data-cy="templateField"
                type="select"
                multiple
                name="templateFields"
              >
                <option value="" key="0" />
                {templateFields
                  ? templateFields.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/templates" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default TemplatesUpdate;
