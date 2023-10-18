import { isGroupUnique, thirdPartyAuth } from 'src/cloud/cloud-functions.js';

describe('Test function: isGroupUnique', () => {
  it('should return a resolved promise when try to save a group that already exists', async () => {
    const Parse = {};
    await expect(isGroupUnique(true, 'myUniqueUnitTestGroupName', null, Parse))
      .resolves
      .toBeUndefined();
  });

  it('should throw an error when try to save a group with an unavailable name', async () => {
    const Parse = {
      Query: () => ({
        equalTo: () => {},
        count: () => Promise.resolve(1),
      }),
    };

    await expect(isGroupUnique(false, 'myUnitTestGroupName', null, Parse))
      .rejects
      .toThrow('Group with this name already exists');
  });

  it('should throw an error when query.count is rejected', async () => {
    const Parse = {
      Query: () => ({
        equalTo: () => {},
        count: () => Promise.reject(new Error('Count err')),
      }),
    };

    await expect(isGroupUnique(false, 'myUnitTestGroupName', null, Parse))
      .rejects
      .toThrow('Count err');
  });

  it('should return a resolved promise when try to save a group with an available name', async () => {
    const Parse = {
      Query: () => ({
        equalTo: () => {},
        count: () => Promise.resolve(0),
      }),
    };

    await expect(isGroupUnique(false, 'myUniqueUnitTestGroupName', null, Parse))
      .resolves
      .toBeUndefined();
  });
});

describe('Test function: thirdPartyAuth', () => {
  it('should return resolved promise with the user when successfully authenticated', async () => {
    const axios = {
      post: () => Promise.resolve({
        data: {
          access_token: 'access_token',
        },
      }),
    };
    class Octokit {
      constructor(auth) {
        this.auth = auth;
      }

      request() {
        return Promise.resolve({
          data: {
            login: 'login',
            name: 'name',
          },
          auth: this.auth,
        });
      }
    }
    const Parse = {
      User: class {
        constructor() {
          this.firstname = 'firstname';
          this.username = 'username';
        }

        set() {
          return this;
        }

        linkWith() {
          return this;
        }
      },
    };

    await expect(thirdPartyAuth(
      'code',
      Parse,
      axios,
      Octokit,
      null,
    )).resolves.toEqual(new Parse.User());
  });
});
