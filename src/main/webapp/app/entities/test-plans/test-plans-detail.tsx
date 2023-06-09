import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './test-plans.reducer';

export const TestPlansDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testPlansEntity = useAppSelector(state => state.testPlans.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testPlansDetailsHeading">Test Plans</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{testPlansEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{testPlansEntity.name}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{testPlansEntity.description}</dd>
          <dt>
            <span id="createdBy">Created By</span>
          </dt>
          <dd>{testPlansEntity.createdBy}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>
            {testPlansEntity.createdAt ? <TextFormat value={testPlansEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>Project</dt>
          <dd>{testPlansEntity.project ? testPlansEntity.project.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/test-plans" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/test-plans/${testPlansEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TestPlansDetail;
