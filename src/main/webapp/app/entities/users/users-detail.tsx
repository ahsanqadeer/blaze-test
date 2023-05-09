import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './users.reducer';

export const UsersDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const usersEntity = useAppSelector(state => state.users.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="usersDetailsHeading">Users</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{usersEntity.id}</dd>
          <dt>
            <span id="firstName">First Name</span>
          </dt>
          <dd>{usersEntity.firstName}</dd>
          <dt>
            <span id="lastName">Last Name</span>
          </dt>
          <dd>{usersEntity.lastName}</dd>
          <dt>
            <span id="password">Password</span>
          </dt>
          <dd>{usersEntity.password}</dd>
          <dt>
            <span id="lastActive">Last Active</span>
          </dt>
          <dd>{usersEntity.lastActive ? <TextFormat value={usersEntity.lastActive} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{usersEntity.status}</dd>
          <dt>
            <span id="createdBy">Created By</span>
          </dt>
          <dd>{usersEntity.createdBy}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>{usersEntity.createdAt ? <TextFormat value={usersEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedBy">Updated By</span>
          </dt>
          <dd>{usersEntity.updatedBy}</dd>
          <dt>
            <span id="updatedAt">Updated At</span>
          </dt>
          <dd>{usersEntity.updatedAt ? <TextFormat value={usersEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="email">Email</span>
          </dt>
          <dd>{usersEntity.email}</dd>
          <dt>
            <span id="isDeleted">Is Deleted</span>
          </dt>
          <dd>{usersEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="emailVerified">Email Verified</span>
          </dt>
          <dd>{usersEntity.emailVerified ? 'true' : 'false'}</dd>
          <dt>
            <span id="provider">Provider</span>
          </dt>
          <dd>{usersEntity.provider}</dd>
          <dt>
            <span id="emailVerificationToken">Email Verification Token</span>
          </dt>
          <dd>{usersEntity.emailVerificationToken}</dd>
          <dt>
            <span id="forgotPasswordToken">Forgot Password Token</span>
          </dt>
          <dd>{usersEntity.forgotPasswordToken}</dd>
          <dt>Company</dt>
          <dd>{usersEntity.company ? usersEntity.company.id : ''}</dd>
          <dt>Project</dt>
          <dd>
            {usersEntity.projects
              ? usersEntity.projects.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {usersEntity.projects && i === usersEntity.projects.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/users" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/users/${usersEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default UsersDetail;
