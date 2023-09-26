import Group from 'src/entities/Group';

describe('Test entity: Group', () => {
  it('Check fields value', () => {
    expect(Group.className).toEqual('Group');
    expect(Group.fields.name.type).toEqual('String');
    expect(Group.fields.name.required).toBe(true);
    expect(Group.fields.users.type).toEqual('Relation');
    expect(Group.fields.users.targetClass).toEqual('_User');
    expect(Group.fields.subgroups.type).toEqual('Relation');
    expect(Group.fields.subgroups.targetClass).toEqual('Group');
    expect(Group.classLevelPermissions).toEqual({
      find: { 'role:admin': true },
      count: { '*': true },
      get: { 'role:admin': true },
      update: { 'role:admin': true },
      create: { 'role:admin': true },
      delete: { 'role:admin': true },
    });
  });
});
