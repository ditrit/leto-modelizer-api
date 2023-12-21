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
