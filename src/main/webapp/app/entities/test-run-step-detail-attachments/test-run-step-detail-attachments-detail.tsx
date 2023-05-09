import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './test-run-step-detail-attachments.reducer';

export const TestRunStepDetailAttachmentsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testRunStepDetailAttachmentsEntity = useAppSelector(state => state.testRunStepDetailAttachments.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testRunStepDetailAttachmentsDetailsHeading">Test Run Step Detail Attachments</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{testRunStepDetailAttachmentsEntity.id}</dd>
          <dt>
            <span id="url">Url</span>
          </dt>
          <dd>{testRunStepDetailAttachmentsEntity.url}</dd>
          <dt>Test Run Step Detail</dt>
          <dd>{testRunStepDetailAttachmentsEntity.testRunStepDetail ? testRunStepDetailAttachmentsEntity.testRunStepDetail.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/test-run-step-detail-attachments" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/test-run-step-detail-attachments/${testRunStepDetailAttachmentsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TestRunStepDetailAttachmentsDetail;
