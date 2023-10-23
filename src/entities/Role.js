/**
 * Definition of the _Role entity.
 * This file follows the format of a Parse.Schema.
 * Any field can be added.
 * Class Level Permission can be granted for a Parse.role, a Parse.User,
 * for authenticated user only (requiresAuthentication: true) or for anyone ('*': true).
 */
const Role = {
  className: '_Role',
  fields: {},
  classLevelPermissions: {
    find: { 'role:admin': true },
    count: { 'role:admin': true },
    get: { 'role:admin': true },
    update: { 'role:admin': true },
    create: { 'role:admin': true },
    delete: { 'role:admin': true },
  },
};

export default Role;
