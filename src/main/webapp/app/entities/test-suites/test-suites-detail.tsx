import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './test-suites.reducer';

export const TestSuitesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testSuitesEntity = useAppSelector(state => state.testSuites.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testSuitesDetailsHeading">Test Suites</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{testSuitesEntity.id}</dd>
          <dt>
            <span id="testSuiteName">Test Suite Name</span>
          </dt>
          <dd>{testSuitesEntity.testSuiteName}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{testSuitesEntity.description}</dd>
          <dt>
            <span id="createdBy">Created By</span>
          </dt>
          <dd>{testSuitesEntity.createdBy}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>
            {testSuitesEntity.createdAt ? <TextFormat value={testSuitesEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedBy">Updated By</span>
          </dt>
          <dd>{testSuitesEntity.updatedBy}</dd>
          <dt>
            <span id="updatedAt">Updated At</span>
          </dt>
          <dd>
            {testSuitesEntity.updatedAt ? <TextFormat value={testSuitesEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>Project</dt>
          <dd>{testSuitesEntity.project ? testSuitesEntity.project.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/test-suites" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/test-suites/${testSuitesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TestSuitesDetail;
