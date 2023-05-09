import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './test-cases.reducer';

export const TestCasesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testCasesEntity = useAppSelector(state => state.testCases.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testCasesDetailsHeading">Test Cases</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{testCasesEntity.id}</dd>
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{testCasesEntity.title}</dd>
          <dt>
            <span id="estimate">Estimate</span>
          </dt>
          <dd>{testCasesEntity.estimate}</dd>
          <dt>
            <span id="createdBy">Created By</span>
          </dt>
          <dd>{testCasesEntity.createdBy}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>
            {testCasesEntity.createdAt ? <TextFormat value={testCasesEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedBy">Updated By</span>
          </dt>
          <dd>{testCasesEntity.updatedBy}</dd>
          <dt>
            <span id="updatedAt">Updated At</span>
          </dt>
          <dd>
            {testCasesEntity.updatedAt ? <TextFormat value={testCasesEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="precondition">Precondition</span>
          </dt>
          <dd>{testCasesEntity.precondition}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{testCasesEntity.description}</dd>
          <dt>
            <span id="isAutomated">Is Automated</span>
          </dt>
          <dd>{testCasesEntity.isAutomated ? 'true' : 'false'}</dd>
          <dt>Test Suite</dt>
          <dd>{testCasesEntity.testSuite ? testCasesEntity.testSuite.id : ''}</dd>
          <dt>Section</dt>
          <dd>{testCasesEntity.section ? testCasesEntity.section.id : ''}</dd>
          <dt>Priority</dt>
          <dd>{testCasesEntity.priority ? testCasesEntity.priority.name : ''}</dd>
          <dt>Template</dt>
          <dd>{testCasesEntity.template ? testCasesEntity.template.id : ''}</dd>
          <dt>Milestone</dt>
          <dd>{testCasesEntity.milestone ? testCasesEntity.milestone.id : ''}</dd>
          <dt>Test Level</dt>
          <dd>
            {testCasesEntity.testLevels
              ? testCasesEntity.testLevels.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {testCasesEntity.testLevels && i === testCasesEntity.testLevels.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/test-cases" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/test-cases/${testCasesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TestCasesDetail;
