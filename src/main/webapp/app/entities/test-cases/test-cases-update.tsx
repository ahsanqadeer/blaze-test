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
import { ISections } from 'app/shared/model/sections.model';
import { getEntities as getSections } from 'app/entities/sections/sections.reducer';
import { ITestCasePriorities } from 'app/shared/model/test-case-priorities.model';
import { getEntities as getTestCasePriorities } from 'app/entities/test-case-priorities/test-case-priorities.reducer';
import { ITemplates } from 'app/shared/model/templates.model';
import { getEntities as getTemplates } from 'app/entities/templates/templates.reducer';
import { IMilestones } from 'app/shared/model/milestones.model';
import { getEntities as getMilestones } from 'app/entities/milestones/milestones.reducer';
import { ITestLevels } from 'app/shared/model/test-levels.model';
import { getEntities as getTestLevels } from 'app/entities/test-levels/test-levels.reducer';
import { ITestCases } from 'app/shared/model/test-cases.model';
import { getEntity, updateEntity, createEntity, reset } from './test-cases.reducer';

export const TestCasesUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const testSuites = useAppSelector(state => state.testSuites.entities);
  const sections = useAppSelector(state => state.sections.entities);
  const testCasePriorities = useAppSelector(state => state.testCasePriorities.entities);
  const templates = useAppSelector(state => state.templates.entities);
  const milestones = useAppSelector(state => state.milestones.entities);
  const testLevels = useAppSelector(state => state.testLevels.entities);
  const testCasesEntity = useAppSelector(state => state.testCases.entity);
  const loading = useAppSelector(state => state.testCases.loading);
  const updating = useAppSelector(state => state.testCases.updating);
  const updateSuccess = useAppSelector(state => state.testCases.updateSuccess);

  const handleClose = () => {
    navigate('/test-cases' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTestSuites({}));
    dispatch(getSections({}));
    dispatch(getTestCasePriorities({}));
    dispatch(getTemplates({}));
    dispatch(getMilestones({}));
    dispatch(getTestLevels({}));
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
      ...testCasesEntity,
      ...values,
      testLevels: mapIdList(values.testLevels),
      testSuite: testSuites.find(it => it.id.toString() === values.testSuite.toString()),
      section: sections.find(it => it.id.toString() === values.section.toString()),
      priority: testCasePriorities.find(it => it.id.toString() === values.priority.toString()),
      template: templates.find(it => it.id.toString() === values.template.toString()),
      milestone: milestones.find(it => it.id.toString() === values.milestone.toString()),
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
          ...testCasesEntity,
          createdAt: convertDateTimeFromServer(testCasesEntity.createdAt),
          updatedAt: convertDateTimeFromServer(testCasesEntity.updatedAt),
          testSuite: testCasesEntity?.testSuite?.id,
          section: testCasesEntity?.section?.id,
          priority: testCasesEntity?.priority?.id,
          template: testCasesEntity?.template?.id,
          milestone: testCasesEntity?.milestone?.id,
          testLevels: testCasesEntity?.testLevels?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="blazeTestApp.testCases.home.createOrEditLabel" data-cy="TestCasesCreateUpdateHeading">
            Create or edit a Test Cases
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="test-cases-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Title"
                id="test-cases-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField
                label="Estimate"
                id="test-cases-estimate"
                name="estimate"
                data-cy="estimate"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField label="Created By" id="test-cases-createdBy" name="createdBy" data-cy="createdBy" type="text" />
              <ValidatedField
                label="Created At"
                id="test-cases-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Updated By" id="test-cases-updatedBy" name="updatedBy" data-cy="updatedBy" type="text" />
              <ValidatedField
                label="Updated At"
                id="test-cases-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Precondition"
                id="test-cases-precondition"
                name="precondition"
                data-cy="precondition"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField label="Description" id="test-cases-description" name="description" data-cy="description" type="textarea" />
              <ValidatedField
                label="Is Automated"
                id="test-cases-isAutomated"
                name="isAutomated"
                data-cy="isAutomated"
                check
                type="checkbox"
              />
              <ValidatedField id="test-cases-testSuite" name="testSuite" data-cy="testSuite" label="Test Suite" type="select">
                <option value="" key="0" />
                {testSuites
                  ? testSuites.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="test-cases-section" name="section" data-cy="section" label="Section" type="select">
                <option value="" key="0" />
                {sections
                  ? sections.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="test-cases-priority" name="priority" data-cy="priority" label="Priority" type="select" required>
                <option value="" key="0" />
                {testCasePriorities
                  ? testCasePriorities.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <ValidatedField id="test-cases-template" name="template" data-cy="template" label="Template" type="select">
                <option value="" key="0" />
                {templates
                  ? templates.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="test-cases-milestone" name="milestone" data-cy="milestone" label="Milestone" type="select">
                <option value="" key="0" />
                {milestones
                  ? milestones.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField label="Test Level" id="test-cases-testLevel" data-cy="testLevel" type="select" multiple name="testLevels">
                <option value="" key="0" />
                {testLevels
                  ? testLevels.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/test-cases" replace color="info">
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

export default TestCasesUpdate;
