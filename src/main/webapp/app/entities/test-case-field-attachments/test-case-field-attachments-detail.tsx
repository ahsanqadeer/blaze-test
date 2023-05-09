import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './test-case-field-attachments.reducer';

export const TestCaseFieldAttachmentsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testCaseFieldAttachmentsEntity = useAppSelector(state => state.testCaseFieldAttachments.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testCaseFieldAttachmentsDetailsHeading">Test Case Field Attachments</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{testCaseFieldAttachmentsEntity.id}</dd>
          <dt>
            <span id="url">Url</span>
          </dt>
          <dd>{testCaseFieldAttachmentsEntity.url}</dd>
          <dt>Test Case Field</dt>
          <dd>{testCaseFieldAttachmentsEntity.testCaseField ? testCaseFieldAttachmentsEntity.testCaseField.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/test-case-field-attachments" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/test-case-field-attachments/${testCaseFieldAttachmentsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TestCaseFieldAttachmentsDetail;
