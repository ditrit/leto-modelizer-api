/**
 * Definition of the Group entity.
 * A Group of users is a set of users and groups that can be used to manage permissions.
 * A group requires a unique name and can have multiple relations with users and groups.
 * The Group object follows the format of a Parse.Schema.
 * Any field can be added.
 * Required fields such as objectId and createdAt are automatically added by Parse.
 * Class Level Permission can be granted for a Parse.role, a Parse.User,
 * for authenticated user only (requiresAuthentication: true) or for anyone ('*': true).
 * For more information, read the documentation at https://docs.parseplatform.org/defined-schema/guide/
 */
const Group = {
  className: 'Group',
  fields: {
    name: { type: 'String', required: true },
    users: { type: 'Relation', targetClass: '_User' },
    subgroups: { type: 'Relation', targetClass: 'Group' },
  },
  classLevelPermissions: {
    find: { 'role:admin': true },
    count: { '*': true },
    get: { 'role:admin': true },
    update: { 'role:admin': true },
    create: { 'role:admin': true },
    delete: { 'role:admin': true },
  },
};

export default Group;
