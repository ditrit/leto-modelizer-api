import Role from 'src/entities/Role';

describe('Test entity: Role', () => {
  it('Check fields value', () => {
    expect(Role.className).toEqual('_Role');
    expect(Role.classLevelPermissions).toEqual({
      find: { 'role:admin': true },
      count: { 'role:admin': true },
      get: { 'role:admin': true },
      update: { 'role:admin': true },
      create: { 'role:admin': true },
      delete: { 'role:admin': true },
    });
  });
});
