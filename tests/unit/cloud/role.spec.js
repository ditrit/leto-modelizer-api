import { deleteAssociatedRole } from 'src/cloud/role.js';

describe('Test function: deleteAssociatedRole', () => {
  it('Should return a resolved promise with nothing', async () => {
    const object = {
      get: () => 'name',
    };
    const Parse = {
      Query: class {
        constructor() {
          this.name = 'name';
        }

        equalTo() {
          this.name = 'name';
          return true;
        }

        first() {
          this.name = '';
          return {
            destroy: () => Promise.resolve(),
          };
        }
      },
    };

    expect(await deleteAssociatedRole(object, Parse)).toEqual();
  });
});
