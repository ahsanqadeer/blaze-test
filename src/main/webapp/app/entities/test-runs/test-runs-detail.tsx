import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './test-runs.reducer';

export const TestRunsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testRunsEntity = useAppSelector(state => state.testRuns.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testRunsDetailsHeading">Test Runs</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{testRunsEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{testRunsEntity.name}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{testRunsEntity.description}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>{testRunsEntity.createdAt ? <TextFormat value={testRunsEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="createdBy">Created By</span>
          </dt>
          <dd>{testRunsEntity.createdBy}</dd>
          <dt>Test Level</dt>
          <dd>{testRunsEntity.testLevel ? testRunsEntity.testLevel.id : ''}</dd>
          <dt>Mile Stone</dt>
          <dd>{testRunsEntity.mileStone ? testRunsEntity.mileStone.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/test-runs" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/test-runs/${testRunsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TestRunsDetail;
