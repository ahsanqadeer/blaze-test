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
import { ITemplateFieldTypes } from 'app/shared/model/template-field-types.model';
import { getEntities as getTemplateFieldTypes } from 'app/entities/template-field-types/template-field-types.reducer';
import { ITemplates } from 'app/shared/model/templates.model';
import { getEntities as getTemplates } from 'app/entities/templates/templates.reducer';
import { ITemplateFields } from 'app/shared/model/template-fields.model';
import { getEntity, updateEntity, createEntity, reset } from './template-fields.reducer';

export const TemplateFieldsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const companies = useAppSelector(state => state.companies.entities);
  const templateFieldTypes = useAppSelector(state => state.templateFieldTypes.entities);
  const templates = useAppSelector(state => state.templates.entities);
  const templateFieldsEntity = useAppSelector(state => state.templateFields.entity);
  const loading = useAppSelector(state => state.templateFields.loading);
  const updating = useAppSelector(state => state.templateFields.updating);
  const updateSuccess = useAppSelector(state => state.templateFields.updateSuccess);

  const handleClose = () => {
    navigate('/template-fields' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCompanies({}));
    dispatch(getTemplateFieldTypes({}));
    dispatch(getTemplates({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...templateFieldsEntity,
      ...values,
      company: companies.find(it => it.id.toString() === values.company.toString()),
      templateFieldType: templateFieldTypes.find(it => it.id.toString() === values.templateFieldType.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...templateFieldsEntity,
          company: templateFieldsEntity?.company?.id,
          templateFieldType: templateFieldsEntity?.templateFieldType?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="blazeTestApp.templateFields.home.createOrEditLabel" data-cy="TemplateFieldsCreateUpdateHeading">
            Create or edit a Template Fields
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="template-fields-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Field Name"
                id="template-fields-fieldName"
                name="fieldName"
                data-cy="fieldName"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField id="template-fields-company" name="company" data-cy="company" label="Company" type="select" required>
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
                id="template-fields-templateFieldType"
                name="templateFieldType"
                data-cy="templateFieldType"
                label="Template Field Type"
                type="select"
                required
              >
                <option value="" key="0" />
                {templateFieldTypes
                  ? templateFieldTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.type}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/template-fields" replace color="info">
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

export default TemplateFieldsUpdate;
