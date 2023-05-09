import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITestLevels } from 'app/shared/model/test-levels.model';
import { getEntities as getTestLevels } from 'app/entities/test-levels/test-levels.reducer';
import { IMilestones } from 'app/shared/model/milestones.model';
import { getEntities as getMilestones } from 'app/entities/milestones/milestones.reducer';
import { ITestRuns } from 'app/shared/model/test-runs.model';
import { getEntity, updateEntity, createEntity, reset } from './test-runs.reducer';

export const TestRunsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const testLevels = useAppSelector(state => state.testLevels.entities);
  const milestones = useAppSelector(state => state.milestones.entities);
  const testRunsEntity = useAppSelector(state => state.testRuns.entity);
  const loading = useAppSelector(state => state.testRuns.loading);
  const updating = useAppSelector(state => state.testRuns.updating);
  const updateSuccess = useAppSelector(state => state.testRuns.updateSuccess);

  const handleClose = () => {
    navigate('/test-runs' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTestLevels({}));
    dispatch(getMilestones({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createdAt = convertDateTimeToServer(values.createdAt);

    const entity = {
      ...testRunsEntity,
      ...values,
      testLevel: testLevels.find(it => it.id.toString() === values.testLevel.toString()),
      mileStone: milestones.find(it => it.id.toString() === values.mileStone.toString()),
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
          ...testRunsEntity,
          createdAt: convertDateTimeFromServer(testRunsEntity.createdAt),
          testLevel: testRunsEntity?.testLevel?.id,
          mileStone: testRunsEntity?.mileStone?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="blazeTestApp.testRuns.home.createOrEditLabel" data-cy="TestRunsCreateUpdateHeading">
            Create or edit a Test Runs
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="test-runs-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Name"
                id="test-runs-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField
                label="Description"
                id="test-runs-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField
                label="Created At"
                id="test-runs-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Created By" id="test-runs-createdBy" name="createdBy" data-cy="createdBy" type="text" />
              <ValidatedField id="test-runs-testLevel" name="testLevel" data-cy="testLevel" label="Test Level" type="select">
                <option value="" key="0" />
                {testLevels
                  ? testLevels.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="test-runs-mileStone" name="mileStone" data-cy="mileStone" label="Mile Stone" type="select">
                <option value="" key="0" />
                {milestones
                  ? milestones.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/test-runs" replace color="info">
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

export default TestRunsUpdate;
