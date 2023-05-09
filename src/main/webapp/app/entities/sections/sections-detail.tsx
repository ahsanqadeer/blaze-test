import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './sections.reducer';

export const SectionsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const sectionsEntity = useAppSelector(state => state.sections.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="sectionsDetailsHeading">Sections</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{sectionsEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{sectionsEntity.name}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{sectionsEntity.description}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>{sectionsEntity.createdAt ? <TextFormat value={sectionsEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="createdBy">Created By</span>
          </dt>
          <dd>{sectionsEntity.createdBy}</dd>
          <dt>
            <span id="updatedAt">Updated At</span>
          </dt>
          <dd>{sectionsEntity.updatedAt ? <TextFormat value={sectionsEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedBy">Updated By</span>
          </dt>
          <dd>{sectionsEntity.updatedBy}</dd>
          <dt>Test Suite</dt>
          <dd>{sectionsEntity.testSuite ? sectionsEntity.testSuite.id : ''}</dd>
          <dt>Parent Section</dt>
          <dd>{sectionsEntity.parentSection ? sectionsEntity.parentSection.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/sections" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sections/${sectionsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SectionsDetail;
