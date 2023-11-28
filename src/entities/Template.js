/**
 * Definition of the Template entity.
 * This file follows the format of a Parse.Schema.
 * Any field can be added.
 * Class Level Permission can be granted for a Parse.role, a Parse.User,
 * for authenticated user only (requiresAuthentication: true) or for anyone ('*': true).
 */
const Template = {
  className: 'Template',
  fields: {
    key: { type: 'String', required: true },
    name: { type: 'String', required: true },
    plugin: { type: 'String', required: false },
    plugins: { type: 'Array', required: false },
    type: { type: 'String', required: true },
    description: { type: 'String', required: false },
    url: { type: 'String', required: false },
    schemas: { type: 'Array', required: false },
    files: { type: 'Array', required: false },
    library: { type: 'Relation', targetClass: 'Library' },
  },
  classLevelPermissions: {
    find: { requiresAuthentication: true },
    count: { requiresAuthentication: true },
    get: { requiresAuthentication: true },
    update: { 'role:admin': true },
    create: { 'role:admin': true },
    delete: { 'role:admin': true },
  },
};

export default Template;
