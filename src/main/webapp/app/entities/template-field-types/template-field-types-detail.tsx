import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './template-field-types.reducer';

export const TemplateFieldTypesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const templateFieldTypesEntity = useAppSelector(state => state.templateFieldTypes.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="templateFieldTypesDetailsHeading">Template Field Types</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{templateFieldTypesEntity.id}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{templateFieldTypesEntity.type}</dd>
          <dt>
            <span id="isList">Is List</span>
          </dt>
          <dd>{templateFieldTypesEntity.isList ? 'true' : 'false'}</dd>
          <dt>
            <span id="attachments">Attachments</span>
          </dt>
          <dd>{templateFieldTypesEntity.attachments ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/template-field-types" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/template-field-types/${templateFieldTypesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TemplateFieldTypesDetail;
