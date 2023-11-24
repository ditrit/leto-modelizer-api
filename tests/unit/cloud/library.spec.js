import { importLibraryTemplates, fillLibraryFields, deleteAssociatedTemplates } from 'src/cloud/library.js';

describe('Test function: importLibraryTemplates', () => {
  it('Should return a resolved promise with nothing if the object did not exist', async () => {
    const library = {
      get: () => 'name',
    };
    const axios = {
      obj: {
        data: {
          templates: [
            {
              key: 'key',
              name: 'name',
            },
          ],
        },
      },
      get: () => axios.obj,
    };
    const Parse = {
      ACL: class {
        constructor() {
          this.name = 'name';
        }

        setPublicReadAccess(bool) {
          this.readAccess = bool;
        }

        setReadAccess(role, bool) {
          this.readAccess = bool;
        }
      },
      Role: class {
        constructor(name, acl) {
          this.name = name;
          this.acl = acl;
        }

        save() {
          this.name = 'name';
          return Promise.resolve();
        }
      },
      Object: {
        extend: () => class Template {
          constructor() {
            this.name = 'name';
          }

          set(objName, obj) {
            this.obj = obj;
          }

          relation() {
            this.name = 'name';
            return {
              add: () => true,
            };
          }

          save() {
            this.name = 'name';
            return true;
          }
        },
      },
    };
    expect(importLibraryTemplates(library, axios, Parse)).resolves.toEqual();
  });
});

describe('Test function: fillLibraryFields', () => {
  it('Should return a resolved promise with nothing if the object did not exist and its url is not already used', async () => {
    const request = {
      object: {
        get: () => 'name',
        set: () => 'name',
        existed: () => false,
        setACL: () => true,
      },
    };
    const axios = {
      obj: {
        data: {
          templates: [
            {
              key: 'key',
              name: 'name',
            },
          ],
        },
      },
      get: () => axios.obj,
    };
    const Parse = {
      ACL: class {
        constructor() {
          this.name = 'name';
        }

        setPublicReadAccess(bool) {
          this.readAccess = bool;
        }

        setReadAccess(role, bool) {
          this.readAccess = bool;
        }

        setWriteAccess(role, bool) {
          this.readAccess = bool;
        }
      },
      Role: class {
        constructor(name, acl) {
          this.name = name;
          this.acl = acl;
        }

        save() {
          this.name = 'name';
          return Promise.resolve();
        }
      },
      Object: {
        extend: () => class Template {
          constructor() {
            this.name = 'name';
          }

          set(objName, obj) {
            this.obj = obj;
          }

          relation() {
            this.name = 'name';
            return {
              add: () => true,
            };
          }

          save() {
            this.name = 'name';
            return true;
          }
        },
      },
      Query: () => ({
        equalTo: () => {},
        count: () => Promise.resolve(0),
      }),
    };
    expect(fillLibraryFields(request, axios, Parse)).resolves.toEqual();
  });

  it('Should throw an error if the object did not exist but its url is already used', async () => {
    let counter = 0;
    const request = {
      object: {
        get: () => 'url',
        set: () => 'name',
        existed: () => false,
        setACL: () => true,
      },
    };
    const axios = {
      obj: {
        data: {
          templates: [
            {
              key: 'key',
              name: 'name',
            },
          ],
        },
      },
      get: () => axios.obj,
    };
    const Parse = {
      ACL: class {
        constructor() {
          this.name = 'name';
        }

        setPublicReadAccess(bool) {
          this.readAccess = bool;
        }

        setReadAccess(role, bool) {
          this.readAccess = bool;
        }

        setWriteAccess(role, bool) {
          this.readAccess = bool;
        }
      },
      Role: class {
        constructor(name, acl) {
          this.name = name;
          this.acl = acl;
        }

        save() {
          this.name = 'name';
          return Promise.resolve();
        }
      },
      Object: {
        extend: () => class Template {
          constructor() {
            this.name = 'name';
          }

          set(objName, obj) {
            this.obj = obj;
          }

          relation() {
            this.name = 'name';
            return {
              add: () => true,
            };
          }

          save() {
            this.name = 'name';
            return true;
          }
        },
      },
      Query: () => ({
        equalTo: () => {},
        count: () => {
          if (counter < 1) {
            counter += 1;
            return Promise.resolve(1);
          }
          return Promise.resolve(0);
        },
      }),
    };
    expect(fillLibraryFields(request, axios, Parse)).rejects.toThrow('Library with this url already exists');
  });

  it('Should throw an error if the object did not exist but its roleName is already used', async () => {
    let counter = 0;
    const request = {
      object: {
        get: () => 'roleName',
        set: () => 'name',
        existed: () => false,
        setACL: () => true,
      },
    };
    const axios = {
      obj: {
        data: {
          templates: [
            {
              key: 'key',
              name: 'name',
            },
          ],
        },
      },
      get: () => axios.obj,
    };
    const Parse = {
      ACL: class {
        constructor() {
          this.name = 'name';
        }

        setPublicReadAccess(bool) {
          this.readAccess = bool;
        }

        setReadAccess(role, bool) {
          this.readAccess = bool;
        }

        setWriteAccess(role, bool) {
          this.readAccess = bool;
        }
      },
      Role: class {
        constructor(name, acl) {
          this.name = name;
          this.acl = acl;
        }

        save() {
          this.name = 'name';
          return Promise.resolve();
        }
      },
      Object: {
        extend: () => class Template {
          constructor() {
            this.name = 'name';
          }

          set(objName, obj) {
            this.obj = obj;
          }

          relation() {
            this.name = 'name';
            return {
              add: () => true,
            };
          }

          save() {
            this.name = 'name';
            return true;
          }
        },
      },
      Query: () => ({
        equalTo: () => {},
        count: () => {
          if (counter < 1) {
            counter += 1;
            return Promise.resolve(0);
          }
          return Promise.resolve(1);
        },
      }),
    };
    expect(fillLibraryFields(request, axios, Parse)).rejects.toThrow('Library with this roleName already exists');
  });

  it('Should throw an error if the roleName has not the right format', async () => {
    const request = {
      object: {
        get: () => 'name%%--',
        set: () => 'name',
        existed: () => false,
        setACL: () => true,
      },
    };
    const axios = {
      obj: {
        data: {
          templates: [
            {
              key: 'key',
              name: 'name',
            },
          ],
        },
      },
      get: () => axios.obj,
    };
    const Parse = {
      ACL: class {
        constructor() {
          this.name = 'name';
        }

        setPublicReadAccess(bool) {
          this.readAccess = bool;
        }

        setReadAccess(role, bool) {
          this.readAccess = bool;
        }

        setWriteAccess(role, bool) {
          this.readAccess = bool;
        }
      },
      Role: class {
        constructor(name, acl) {
          this.name = name;
          this.acl = acl;
        }

        save() {
          this.name = 'name';
          return Promise.resolve();
        }
      },
      Object: {
        extend: () => class Template {
          constructor() {
            this.name = 'name';
          }

          set(objName, obj) {
            this.obj = obj;
          }

          relation() {
            this.name = 'name';
            return {
              add: () => true,
            };
          }

          save() {
            this.name = 'name';
            return true;
          }
        },
      },
      Query: () => ({
        equalTo: () => {},
        count: () => Promise.resolve(0),
      }),
    };
    expect(fillLibraryFields(request, axios, Parse)).rejects.toThrow('roleName must contain only lower case alphanumerics and underscores');
  });

  it('Should return a resolved promise with nothing if the object already existed', async () => {
    const request = {
      object: {
        get: () => 'name',
        set: () => 'name',
        existed: () => true,
      },
    };
    const axios = {
      obj: {
        data: {
          templates: [
            {
              key: 'key',
              name: 'name',
            },
          ],
        },
      },
      get: () => axios.obj,
    };
    const Parse = {
      Object: {
        extend: () => class Template {
          constructor() {
            this.name = 'name';
          }

          relation() {
            this.name = 'name';
            return {
              query() {
                return {
                  equalTo() {
                    return {
                      find: () => Promise.resolve([new Template()]),
                    };
                  },
                };
              },
              remove: () => true,
            };
          }

          save() {
            this.name = 'name';
            return true;
          }

          destroy() {
            this.name = 'name';
            return true;
          }
        },
      },
      Query: class {
        equalTo() {
          this.name = 'name';
          return true;
        }

        find() {
          this.name = 'name';
          const Template = Parse.Object.extend('Template');
          return Promise.resolve([new Template()]);
        }
      },
    };
    expect(fillLibraryFields(request, axios, Parse)).resolves.toEqual();
  });
});

describe('Test function: deleteAssociatedTemplates', () => {
  it('Should return a resolved promise with nothing', async () => {
    const request = {
      object: {
        get: () => 'name',
      },
    };
    const Parse = {
      Object: {
        extend: () => class Template {
          constructor() {
            this.name = 'name';
          }

          relation() {
            this.name = 'name';
            return {
              query() {
                return {
                  equalTo() {
                    return {
                      find: () => Promise.resolve([new Template()]),
                    };
                  },
                };
              },
              remove: () => true,
            };
          }

          save() {
            this.name = 'name';
            return true;
          }

          destroy() {
            this.name = 'name';
            return true;
          }
        },
      },
      Query: class {
        equalTo() {
          this.name = 'name';
          return true;
        }

        find() {
          this.name = 'name';
          const Template = Parse.Object.extend('Template');
          return Promise.resolve([new Template()]);
        }
      },
    };

    expect(await deleteAssociatedTemplates(request, Parse)).toEqual();
  });
});
