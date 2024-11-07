import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './car.reducer';

export const CarDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const carEntity = useAppSelector(state => state.car.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="carDetailsHeading">Car</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{carEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{carEntity.name}</dd>
          <dt>
            <span id="model">Model</span>
          </dt>
          <dd>{carEntity.model}</dd>
          <dt>
            <span id="price">Price</span>
          </dt>
          <dd>{carEntity.price}</dd>
          <dt>Owner</dt>
          <dd>{carEntity.owner ? carEntity.owner.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/car" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/car/${carEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CarDetail;
