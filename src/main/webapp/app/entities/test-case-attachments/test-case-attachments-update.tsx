import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITestCases } from 'app/shared/model/test-cases.model';
import { getEntities as getTestCases } from 'app/entities/test-cases/test-cases.reducer';
import { ITestCaseAttachments } from 'app/shared/model/test-case-attachments.model';
import { getEntity, updateEntity, createEntity, reset } from './test-case-attachments.reducer';

export const TestCaseAttachmentsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const testCases = useAppSelector(state => state.testCases.entities);
  const testCaseAttachmentsEntity = useAppSelector(state => state.testCaseAttachments.entity);
  const loading = useAppSelector(state => state.testCaseAttachments.loading);
  const updating = useAppSelector(state => state.testCaseAttachments.updating);
  const updateSuccess = useAppSelector(state => state.testCaseAttachments.updateSuccess);

  const handleClose = () => {
    navigate('/test-case-attachments' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTestCases({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...testCaseAttachmentsEntity,
      ...values,
      testCase: testCases.find(it => it.id.toString() === values.testCase.toString()),
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
          ...testCaseAttachmentsEntity,
          testCase: testCaseAttachmentsEntity?.testCase?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="blazeTestApp.testCaseAttachments.home.createOrEditLabel" data-cy="TestCaseAttachmentsCreateUpdateHeading">
            Create or edit a Test Case Attachments
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
                <ValidatedField name="id" required readOnly id="test-case-attachments-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Url"
                id="test-case-attachments-url"
                name="url"
                data-cy="url"
                type="textarea"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                id="test-case-attachments-testCase"
                name="testCase"
                data-cy="testCase"
                label="Test Case"
                type="select"
                required
              >
                <option value="" key="0" />
                {testCases
                  ? testCases.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/test-case-attachments" replace color="info">
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

export default TestCaseAttachmentsUpdate;
