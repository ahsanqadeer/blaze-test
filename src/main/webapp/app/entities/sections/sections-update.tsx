import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITestSuites } from 'app/shared/model/test-suites.model';
import { getEntities as getTestSuites } from 'app/entities/test-suites/test-suites.reducer';
import { getEntities as getSections } from 'app/entities/sections/sections.reducer';
import { ISections } from 'app/shared/model/sections.model';
import { getEntity, updateEntity, createEntity, reset } from './sections.reducer';

export const SectionsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const testSuites = useAppSelector(state => state.testSuites.entities);
  const sections = useAppSelector(state => state.sections.entities);
  const sectionsEntity = useAppSelector(state => state.sections.entity);
  const loading = useAppSelector(state => state.sections.loading);
  const updating = useAppSelector(state => state.sections.updating);
  const updateSuccess = useAppSelector(state => state.sections.updateSuccess);

  const handleClose = () => {
    navigate('/sections' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTestSuites({}));
    dispatch(getSections({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...sectionsEntity,
      ...values,
      testSuite: testSuites.find(it => it.id.toString() === values.testSuite.toString()),
      parentSection: sections.find(it => it.id.toString() === values.parentSection.toString()),
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
          updatedAt: displayDefaultDateTime(),
        }
      : {
          ...sectionsEntity,
          createdAt: convertDateTimeFromServer(sectionsEntity.createdAt),
          updatedAt: convertDateTimeFromServer(sectionsEntity.updatedAt),
          testSuite: sectionsEntity?.testSuite?.id,
          parentSection: sectionsEntity?.parentSection?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="blazeTestApp.sections.home.createOrEditLabel" data-cy="SectionsCreateUpdateHeading">
            Create or edit a Sections
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="sections-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Name"
                id="sections-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField label="Description" id="sections-description" name="description" data-cy="description" type="textarea" />
              <ValidatedField
                label="Created At"
                id="sections-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Created By" id="sections-createdBy" name="createdBy" data-cy="createdBy" type="text" />
              <ValidatedField
                label="Updated At"
                id="sections-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Updated By" id="sections-updatedBy" name="updatedBy" data-cy="updatedBy" type="text" />
              <ValidatedField id="sections-testSuite" name="testSuite" data-cy="testSuite" label="Test Suite" type="select">
                <option value="" key="0" />
                {testSuites
                  ? testSuites.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="sections-parentSection" name="parentSection" data-cy="parentSection" label="Parent Section" type="select">
                <option value="" key="0" />
                {sections
                  ? sections.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/sections" replace color="info">
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

export default SectionsUpdate;
