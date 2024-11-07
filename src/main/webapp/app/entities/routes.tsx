import React from 'react';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Car from './car';
import Owner from './owner';
import { Route } from 'react-router';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="car/*" element={<Car />} />
        <Route path="owner/*" element={<Owner />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
