import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITestCaseFields } from 'app/shared/model/test-case-fields.model';
import { getEntities as getTestCaseFields } from 'app/entities/test-case-fields/test-case-fields.reducer';
import { ITestCaseFieldAttachments } from 'app/shared/model/test-case-field-attachments.model';
import { getEntity, updateEntity, createEntity, reset } from './test-case-field-attachments.reducer';

export const TestCaseFieldAttachmentsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const testCaseFields = useAppSelector(state => state.testCaseFields.entities);
  const testCaseFieldAttachmentsEntity = useAppSelector(state => state.testCaseFieldAttachments.entity);
  const loading = useAppSelector(state => state.testCaseFieldAttachments.loading);
  const updating = useAppSelector(state => state.testCaseFieldAttachments.updating);
  const updateSuccess = useAppSelector(state => state.testCaseFieldAttachments.updateSuccess);

  const handleClose = () => {
    navigate('/test-case-field-attachments' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTestCaseFields({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...testCaseFieldAttachmentsEntity,
      ...values,
      testCaseField: testCaseFields.find(it => it.id.toString() === values.testCaseField.toString()),
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
          ...testCaseFieldAttachmentsEntity,
          testCaseField: testCaseFieldAttachmentsEntity?.testCaseField?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="blazeTestApp.testCaseFieldAttachments.home.createOrEditLabel" data-cy="TestCaseFieldAttachmentsCreateUpdateHeading">
            Create or edit a Test Case Field Attachments
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
                <ValidatedField name="id" required readOnly id="test-case-field-attachments-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Url"
                id="test-case-field-attachments-url"
                name="url"
                data-cy="url"
                type="textarea"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                id="test-case-field-attachments-testCaseField"
                name="testCaseField"
                data-cy="testCaseField"
                label="Test Case Field"
                type="select"
                required
              >
                <option value="" key="0" />
                {testCaseFields
                  ? testCaseFields.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/test-case-field-attachments" replace color="info">
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

export default TestCaseFieldAttachmentsUpdate;
