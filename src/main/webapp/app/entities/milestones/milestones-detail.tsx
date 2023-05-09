import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './milestones.reducer';

export const MilestonesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const milestonesEntity = useAppSelector(state => state.milestones.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="milestonesDetailsHeading">Milestones</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{milestonesEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{milestonesEntity.name}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{milestonesEntity.description}</dd>
          <dt>
            <span id="reference">Reference</span>
          </dt>
          <dd>{milestonesEntity.reference}</dd>
          <dt>
            <span id="startDate">Start Date</span>
          </dt>
          <dd>
            {milestonesEntity.startDate ? <TextFormat value={milestonesEntity.startDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="endDate">End Date</span>
          </dt>
          <dd>{milestonesEntity.endDate ? <TextFormat value={milestonesEntity.endDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="isCompleted">Is Completed</span>
          </dt>
          <dd>{milestonesEntity.isCompleted ? 'true' : 'false'}</dd>
          <dt>Parent Milestone</dt>
          <dd>{milestonesEntity.parentMilestone ? milestonesEntity.parentMilestone.id : ''}</dd>
          <dt>Project</dt>
          <dd>{milestonesEntity.project ? milestonesEntity.project.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/milestones" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/milestones/${milestonesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default MilestonesDetail;
