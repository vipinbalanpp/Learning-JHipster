import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './owner.reducer';

export const OwnerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ownerEntity = useAppSelector(state => state.owner.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ownerDetailsHeading">Owner</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{ownerEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{ownerEntity.name}</dd>
          <dt>
            <span id="gender">Gender</span>
          </dt>
          <dd>{ownerEntity.gender}</dd>
        </dl>
        <Button tag={Link} to="/owner" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/owner/${ownerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default OwnerDetail;
