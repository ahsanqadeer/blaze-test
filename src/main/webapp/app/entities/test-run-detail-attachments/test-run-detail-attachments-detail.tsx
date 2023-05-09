import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './test-run-detail-attachments.reducer';

export const TestRunDetailAttachmentsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testRunDetailAttachmentsEntity = useAppSelector(state => state.testRunDetailAttachments.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testRunDetailAttachmentsDetailsHeading">Test Run Detail Attachments</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{testRunDetailAttachmentsEntity.id}</dd>
          <dt>
            <span id="url">Url</span>
          </dt>
          <dd>{testRunDetailAttachmentsEntity.url}</dd>
          <dt>Test Run Detail</dt>
          <dd>{testRunDetailAttachmentsEntity.testRunDetail ? testRunDetailAttachmentsEntity.testRunDetail.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/test-run-detail-attachments" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/test-run-detail-attachments/${testRunDetailAttachmentsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TestRunDetailAttachmentsDetail;
