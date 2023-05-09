import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITestRunStepDetails } from 'app/shared/model/test-run-step-details.model';
import { getEntities as getTestRunStepDetails } from 'app/entities/test-run-step-details/test-run-step-details.reducer';
import { ITestRunStepDetailAttachments } from 'app/shared/model/test-run-step-detail-attachments.model';
import { getEntity, updateEntity, createEntity, reset } from './test-run-step-detail-attachments.reducer';

export const TestRunStepDetailAttachmentsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const testRunStepDetails = useAppSelector(state => state.testRunStepDetails.entities);
  const testRunStepDetailAttachmentsEntity = useAppSelector(state => state.testRunStepDetailAttachments.entity);
  const loading = useAppSelector(state => state.testRunStepDetailAttachments.loading);
  const updating = useAppSelector(state => state.testRunStepDetailAttachments.updating);
  const updateSuccess = useAppSelector(state => state.testRunStepDetailAttachments.updateSuccess);

  const handleClose = () => {
    navigate('/test-run-step-detail-attachments' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTestRunStepDetails({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...testRunStepDetailAttachmentsEntity,
      ...values,
      testRunStepDetail: testRunStepDetails.find(it => it.id.toString() === values.testRunStepDetail.toString()),
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
          ...testRunStepDetailAttachmentsEntity,
          testRunStepDetail: testRunStepDetailAttachmentsEntity?.testRunStepDetail?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2
            id="blazeTestApp.testRunStepDetailAttachments.home.createOrEditLabel"
            data-cy="TestRunStepDetailAttachmentsCreateUpdateHeading"
          >
            Create or edit a Test Run Step Detail Attachments
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
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="test-run-step-detail-attachments-id"
                  label="ID"
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label="Url"
                id="test-run-step-detail-attachments-url"
                name="url"
                data-cy="url"
                type="textarea"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                id="test-run-step-detail-attachments-testRunStepDetail"
                name="testRunStepDetail"
                data-cy="testRunStepDetail"
                label="Test Run Step Detail"
                type="select"
                required
              >
                <option value="" key="0" />
                {testRunStepDetails
                  ? testRunStepDetails.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <Button
                tag={Link}
                id="cancel-save"
                data-cy="entityCreateCancelButton"
                to="/test-run-step-detail-attachments"
                replace
                color="info"
              >
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

export default TestRunStepDetailAttachmentsUpdate;
