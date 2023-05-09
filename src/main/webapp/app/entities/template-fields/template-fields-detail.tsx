import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './template-fields.reducer';

export const TemplateFieldsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const templateFieldsEntity = useAppSelector(state => state.templateFields.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="templateFieldsDetailsHeading">Template Fields</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{templateFieldsEntity.id}</dd>
          <dt>
            <span id="fieldName">Field Name</span>
          </dt>
          <dd>{templateFieldsEntity.fieldName}</dd>
          <dt>Company</dt>
          <dd>{templateFieldsEntity.company ? templateFieldsEntity.company.id : ''}</dd>
          <dt>Template Field Type</dt>
          <dd>{templateFieldsEntity.templateFieldType ? templateFieldsEntity.templateFieldType.type : ''}</dd>
        </dl>
        <Button tag={Link} to="/template-fields" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/template-fields/${templateFieldsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TemplateFieldsDetail;
