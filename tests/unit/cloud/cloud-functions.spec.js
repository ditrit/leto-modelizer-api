import { thirdPartyAuth } from 'src/cloud/cloud-functions.js';

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
