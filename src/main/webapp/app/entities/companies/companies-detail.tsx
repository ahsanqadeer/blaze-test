import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './companies.reducer';

export const CompaniesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const companiesEntity = useAppSelector(state => state.companies.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="companiesDetailsHeading">Companies</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{companiesEntity.id}</dd>
          <dt>
            <span id="country">Country</span>
          </dt>
          <dd>{companiesEntity.country}</dd>
          <dt>
            <span id="url">Url</span>
          </dt>
          <dd>{companiesEntity.url}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{companiesEntity.name}</dd>
          <dt>
            <span id="expectedNoOfUsers">Expected No Of Users</span>
          </dt>
          <dd>{companiesEntity.expectedNoOfUsers}</dd>
          <dt>
            <span id="createdBy">Created By</span>
          </dt>
          <dd>{companiesEntity.createdBy}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>
            {companiesEntity.createdAt ? <TextFormat value={companiesEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedBy">Updated By</span>
          </dt>
          <dd>{companiesEntity.updatedBy}</dd>
          <dt>
            <span id="updatedAt">Updated At</span>
          </dt>
          <dd>
            {companiesEntity.updatedAt ? <TextFormat value={companiesEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/companies" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/companies/${companiesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CompaniesDetail;
