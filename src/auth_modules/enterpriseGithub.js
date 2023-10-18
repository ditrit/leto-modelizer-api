/* eslint-disable class-methods-use-this */
// Helper functions for accessing an enterprise-Github API.
// This file is based on the original Github API module for Parse.
import Parse from 'parse';
import httpsRequest from '../utils/httpsRequest.js';

/**
 * A promisey wrapper for api requests.
 * @param { string } path - Path to make the request.
 * @param { string } accessToken - Access token to be validated.
 * @param { string } host - The host of the target API.
 * @param { string } endpoint - The endpoint of the target API.
 * @returns { Promise } - Resolved if access token is valid and rejected if not.
 */
function request(path, accessToken, host, endpoint) {
  return httpsRequest.get({
    host,
    path: endpoint + path,
    headers: {
      Authorization: `bearer ${accessToken}`,
      'User-Agent': 'parse-server',
    },
  });
}

class EnterpriseGithub {
  constructor(baseURL) {
    if (baseURL) {
      const { host, endpoint } = /^https:\/\/(?<host>.+)\/(?<endpoint>[^/]+)$/.exec(
        baseURL,
      ).groups;
      this.host = host;
      this.endpoint = endpoint;
    }
  }

  /**
   * Returns a promise that fulfills if this user id is valid.
   * @param { object } authData - Data of the authenticated user
   * @returns { Promise } - Resolved if access token corresponds to the user ID and rejected if not.
   */
  validateAuthData(authData) {
    return request('user', authData.access_token, this.host, this.endpoint).then((data) => {
      if (data && data.id === authData.id) {
        return;
      }
      throw new Parse.Error(Parse.Error.OBJECT_NOT_FOUND, 'Authentication is invalid for this user.');
    })
      .catch(() => { throw new Error('Request cannot be performed, check that you have access to the given address'); });
  }

  /**
   * Returns a promise that fulfills if this app id is valid.
   * @returns { Promise } Resolved if this app id is valid and rejected if not.
   */
  validateAppId() {
    return Promise.resolve();
  }
}

export default EnterpriseGithub;
