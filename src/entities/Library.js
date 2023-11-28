/**
 * Definition of the Library entity.
 * This file follows the format of a Parse.Schema.
 * Any field can be added.
 * Class Level Permission can be granted for a Parse.role, a Parse.User,
 * for authenticated user only (requiresAuthentication: true) or for anyone ('*': true).
 * - Fields:
 * name {string} - name of the library imported from the raw data.
 * url {string} - url to the raw data to import the templates.
 * description {string} - description of the library imported from the raw data.
 * author {string} - author of the library imported from the raw data.
 * version {string} - version of the library imported from the raw data.
 * roleName {string} - unique field set by the user when importing the library
 * and used to name the associated role.
 */
const Library = {
  className: 'Library',
  fields: {
    name: { type: 'String', required: true },
    url: { type: 'String', required: true },
    description: { type: 'String', required: false },
    author: { type: 'String', required: true },
    version: { type: 'String', required: true },
    roleName: { type: 'String', required: true },
  },
  classLevelPermissions: {
    find: { requiresAuthentication: true },
    count: { requiresAuthentication: true },
    get: { requiresAuthentication: true },
    update: { requiresAuthentication: true },
    create: { requiresAuthentication: true },
    delete: { 'role:admin': true },
  },
};

export default Library;
