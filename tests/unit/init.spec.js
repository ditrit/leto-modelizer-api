import { setupDefaultRoles } from '../../src/init.js';

describe('Test function: setupDefaultRoles', () => {
  it('should return a resolved promise with array containing false for each default roles if query fails', async () => {
    const Parse = {
      initialize: () => true,
      Role: class {
        constructor() {
          this.name = 'name';
        }
      },
      Query: class {
        constructor() {
          this.name = 'name';
        }

        equalTo() {
          this.name = 'name';
          return true;
        }

        first() {
          this.name = 'name';
          throw new Error();
        }
      },
    };
    const configuration = {
      parseServer: {
        appId: 'appId',
        masterKey: 'masterKey',
        serverURL: 'URL',
      },
    };

    expect(setupDefaultRoles(configuration, Parse)).resolves.toEqual([
      false,
      false,
      false,
      false,
      false,
      false,
    ]);
  });

  it('should return resolved promise with array containing false for each default roles if the save fails', async () => {
    const Parse = {
      initialize: () => true,
      Role: class {
        constructor() {
          this.name = 'name';
        }

        set() {
          this.name = 'name';
        }

        setACL() {
          this.name = 'name';
        }

        save() {
          this.name = null;
          throw new Error();
        }
      },
      Query: class {
        constructor() {
          this.name = 'name';
        }

        equalTo() {
          this.name = 'name';
          return true;
        }

        first() {
          this.name = 'name';
          return Promise.resolve();
        }
      },
      ACL: class {
        constructor() {
          this.name = 'name';
        }

        setPublicReadAccess() {
          this.name = 'name';
        }

        setPublicWriteAccess() {
          this.name = 'name';
        }
      },
    };
    const configuration = {
      parseServer: {
        appId: 'appId',
        masterKey: 'masterKey',
        serverURL: 'URL',
      },
    };

    expect(setupDefaultRoles(configuration, Parse)).resolves.toEqual([
      false,
      false,
      false,
      false,
      false,
      false,
    ]);
  });

  it('should return a resolved promise with array containing false if admin and CF_createDiagram roles already exist', async () => {
    const Parse = {
      initialize: () => true,
      Role: class {
        constructor() {
          this.name = 'name';
        }
      },
      Query: class {
        constructor() {
          this.name = 'name';
        }

        equalTo() {
          this.name = 'name';
          return true;
        }

        first() {
          this.name = 'name';
          return Promise.resolve(new Parse.Role());
        }
      },
    };
    const configuration = {
      parseServer: {
        appId: 'appId',
        masterKey: 'masterKey',
        serverURL: 'URL',
      },
    };

    expect(setupDefaultRoles(configuration, Parse)).resolves.toEqual([
      false,
      false,
      false,
      false,
      false,
      false,
    ]);
  });

  it('should return a resolved promise with true if admin and CF_createDiagram roles do not exist', async () => {
    const Parse = {
      initialize: () => true,
      Role: class {
        constructor() {
          this.name = 'name';
        }

        set() {
          this.name = 'name';
        }

        setACL() {
          this.name = 'name';
        }

        save() {
          return Promise.resolve(this);
        }
      },
      Query: class {
        constructor() {
          this.name = 'name';
        }

        equalTo() {
          this.name = 'name';
          return true;
        }

        first() {
          this.name = 'name';
          return Promise.resolve();
        }
      },
      ACL: class {
        constructor() {
          this.name = 'name';
        }

        setPublicReadAccess() {
          this.name = 'name';
        }

        setPublicWriteAccess() {
          this.name = 'name';
        }
      },
    };
    const configuration = {
      parseServer: {
        appId: 'appId',
        masterKey: 'masterKey',
        serverURL: 'URL',
      },
    };

    expect(setupDefaultRoles(configuration, Parse)).resolves.toEqual([
      true,
      true,
      true,
      true,
      true,
      true,
    ]);
  });
});
