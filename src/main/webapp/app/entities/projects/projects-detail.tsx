import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './projects.reducer';

export const ProjectsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const projectsEntity = useAppSelector(state => state.projects.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="projectsDetailsHeading">Projects</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{projectsEntity.id}</dd>
          <dt>
            <span id="projectName">Project Name</span>
          </dt>
          <dd>{projectsEntity.projectName}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{projectsEntity.description}</dd>
          <dt>
            <span id="isactive">Isactive</span>
          </dt>
          <dd>{projectsEntity.isactive ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">Created By</span>
          </dt>
          <dd>{projectsEntity.createdBy}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>{projectsEntity.createdAt ? <TextFormat value={projectsEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedBy">Updated By</span>
          </dt>
          <dd>{projectsEntity.updatedBy}</dd>
          <dt>
            <span id="updatedAt">Updated At</span>
          </dt>
          <dd>{projectsEntity.updatedAt ? <TextFormat value={projectsEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>Default Template</dt>
          <dd>{projectsEntity.defaultTemplate ? projectsEntity.defaultTemplate.id : ''}</dd>
          <dt>Company</dt>
          <dd>{projectsEntity.company ? projectsEntity.company.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/projects" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/projects/${projectsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProjectsDetail;
