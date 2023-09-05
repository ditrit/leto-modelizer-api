/**
 * Definition of the _User entity.
 * This file follows the format of a Parse.Schema.
 * Any field can be added.
 * Class Level Permission can be granted for a Parse.role, a Parse.User,
 * for authenticated user only (requiresAuthentication: true) or for anyone ('*': true).
 */
const User = {
  className: '_User',
  fields: {
    firstname: { type: 'String', required: true },
    lastname: { type: 'String', required: false },
  },
  classLevelPermissions: {
    find: { 'role:admin': true },
    count: { 'role:admin': true },
    get: { 'role:admin': true },
    update: { 'role:admin': true },
    create: { 'role:admin': true },
    delete: { 'role:admin': true },
    protectedFields: {
      // These fields will be protected from all other users.
      // AuthData and password are already protected by default
      '*': ['authData', 'emailVerified', 'password', 'username'],
    },
  },
};

export default User;
