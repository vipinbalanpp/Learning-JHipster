import MenuItem from 'app/shared/layout/menus/menu-item';
import React from 'react';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/car">
        Car
      </MenuItem>
      <MenuItem icon="asterisk" to="/owner">
        Owner
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
