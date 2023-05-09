import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './test-case-attachments.reducer';

export const TestCaseAttachmentsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testCaseAttachmentsEntity = useAppSelector(state => state.testCaseAttachments.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testCaseAttachmentsDetailsHeading">Test Case Attachments</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{testCaseAttachmentsEntity.id}</dd>
          <dt>
            <span id="url">Url</span>
          </dt>
          <dd>{testCaseAttachmentsEntity.url}</dd>
          <dt>Test Case</dt>
          <dd>{testCaseAttachmentsEntity.testCase ? testCaseAttachmentsEntity.testCase.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/test-case-attachments" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/test-case-attachments/${testCaseAttachmentsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TestCaseAttachmentsDetail;
