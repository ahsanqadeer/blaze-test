import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { byteSize, Translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITestCases } from 'app/shared/model/test-cases.model';
import { getEntities } from './test-cases.reducer';

export const TestCases = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const testCasesList = useAppSelector(state => state.testCases.entities);
  const loading = useAppSelector(state => state.testCases.loading);
  const totalItems = useAppSelector(state => state.testCases.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  return (
    <div>
      <h2 id="test-cases-heading" data-cy="TestCasesHeading">
        Test Cases
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/test-cases/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Test Cases
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {testCasesList && testCasesList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('title')}>
                  Title <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('estimate')}>
                  Estimate <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  Created By <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  Created At <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('updatedBy')}>
                  Updated By <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('updatedAt')}>
                  Updated At <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('precondition')}>
                  Precondition <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('description')}>
                  Description <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('isAutomated')}>
                  Is Automated <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Test Suite <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Section <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Priority <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Template <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Milestone <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {testCasesList.map((testCases, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/test-cases/${testCases.id}`} color="link" size="sm">
                      {testCases.id}
                    </Button>
                  </td>
                  <td>{testCases.title}</td>
                  <td>{testCases.estimate}</td>
                  <td>{testCases.createdBy}</td>
                  <td>{testCases.createdAt ? <TextFormat type="date" value={testCases.createdAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{testCases.updatedBy}</td>
                  <td>{testCases.updatedAt ? <TextFormat type="date" value={testCases.updatedAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{testCases.precondition}</td>
                  <td>{testCases.description}</td>
                  <td>{testCases.isAutomated ? 'true' : 'false'}</td>
                  <td>{testCases.testSuite ? <Link to={`/test-suites/${testCases.testSuite.id}`}>{testCases.testSuite.id}</Link> : ''}</td>
                  <td>{testCases.section ? <Link to={`/sections/${testCases.section.id}`}>{testCases.section.id}</Link> : ''}</td>
                  <td>
                    {testCases.priority ? <Link to={`/test-case-priorities/${testCases.priority.id}`}>{testCases.priority.name}</Link> : ''}
                  </td>
                  <td>{testCases.template ? <Link to={`/templates/${testCases.template.id}`}>{testCases.template.id}</Link> : ''}</td>
                  <td>{testCases.milestone ? <Link to={`/milestones/${testCases.milestone.id}`}>{testCases.milestone.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/test-cases/${testCases.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/test-cases/${testCases.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/test-cases/${testCases.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Test Cases found</div>
        )}
      </div>
      {totalItems ? (
        <div className={testCasesList && testCasesList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default TestCases;
