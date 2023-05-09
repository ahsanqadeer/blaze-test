import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './test-case-fields.reducer';

export const TestCaseFieldsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testCaseFieldsEntity = useAppSelector(state => state.testCaseFields.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testCaseFieldsDetailsHeading">Test Case Fields</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{testCaseFieldsEntity.id}</dd>
          <dt>
            <span id="expectedResult">Expected Result</span>
          </dt>
          <dd>{testCaseFieldsEntity.expectedResult}</dd>
          <dt>
            <span id="value">Value</span>
          </dt>
          <dd>{testCaseFieldsEntity.value}</dd>
          <dt>Template Field</dt>
          <dd>{testCaseFieldsEntity.templateField ? testCaseFieldsEntity.templateField.id : ''}</dd>
          <dt>Test Case</dt>
          <dd>{testCaseFieldsEntity.testCase ? testCaseFieldsEntity.testCase.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/test-case-fields" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/test-case-fields/${testCaseFieldsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TestCaseFieldsDetail;
