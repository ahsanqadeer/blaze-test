import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/companies">
        Companies
      </MenuItem>
      <MenuItem icon="asterisk" to="/milestones">
        Milestones
      </MenuItem>
      <MenuItem icon="asterisk" to="/permissions">
        Permissions
      </MenuItem>
      <MenuItem icon="asterisk" to="/projects">
        Projects
      </MenuItem>
      <MenuItem icon="asterisk" to="/roles">
        Roles
      </MenuItem>
      <MenuItem icon="asterisk" to="/sections">
        Sections
      </MenuItem>
      <MenuItem icon="asterisk" to="/template-field-types">
        Template Field Types
      </MenuItem>
      <MenuItem icon="asterisk" to="/template-fields">
        Template Fields
      </MenuItem>
      <MenuItem icon="asterisk" to="/templates">
        Templates
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-case-attachments">
        Test Case Attachments
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-case-field-attachments">
        Test Case Field Attachments
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-case-fields">
        Test Case Fields
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-case-priorities">
        Test Case Priorities
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-cases">
        Test Cases
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-levels">
        Test Levels
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-plans">
        Test Plans
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-run-detail-attachments">
        Test Run Detail Attachments
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-run-details">
        Test Run Details
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-run-step-detail-attachments">
        Test Run Step Detail Attachments
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-run-step-details">
        Test Run Step Details
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-runs">
        Test Runs
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-statuses">
        Test Statuses
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-suites">
        Test Suites
      </MenuItem>
      <MenuItem icon="asterisk" to="/users">
        Users
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
