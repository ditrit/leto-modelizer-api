import { loadConfiguration } from '../services/ConfigurationService.js';

const configuration = loadConfiguration();

/**
 * This function makes the call to the GitHub/enterprise GitHub API to retrieve his information.
 * @param {string} accessToken - Access token of the authenticated user.
 * @param {object} Parse - The Parse object to use SDK functionalities.
 * @param {*} Octokit - Octokit class to interact with the GitHub API
 * @param {object} enterpriseServer36Admin - Used to configure Octokit with enterprise GitHub API.
 * @returns {Parse.User} The authenticated User in Parse format.
 */
async function apiRequest(accessToken, Parse, Octokit, enterpriseServer36Admin) {
  let octokit;

  if (!configuration.isEnterpriseGitHub) {
    octokit = new Octokit({
      auth: accessToken,
    });
  } else {
    const OctokitEnterprise36 = Octokit.plugin(enterpriseServer36Admin);

    octokit = new OctokitEnterprise36({
      auth: accessToken,
      baseUrl: configuration.baseURL,
    });
  }

  return octokit.request('GET /user', {
    headers: {
      'X-GitHub-Api-Version': '2022-11-28',
    },
  }).then(
    async (response) => {
      const user = new Parse.User();

      user.set('username', response.data.login);
      user.set('firstname', response.data.name);

      return user.linkWith(
        configuration.isEnterpriseGitHub ? 'enterpriseGithub' : 'github',
        {
          authData: {
            id: response.data.id,
            access_token: accessToken,
          },
        },
        { useMasterKey: true },
      );
    },
  );
}

/**
 * This function makes the requests to authenticate a User via GitHub or an Enterprise
 * Github and store his data in the Parse Server.
 * @param {string} temporaryCode - Temporary code obtained when authenticated via
 * GitHub/enterpriseGitHub endpoint and used to get the access_token.
 * @param {object} Parse - The Parse object to use SDK functionalities.
 * @param {object} axios - The axios lib to make requests.
 * @param {*} Octokit - Octokit class to interact with the GitHub API
 * @param {object} enterpriseServer36Admin - Used to configure Octokit with enterprise GitHub API.
 * @returns {Parse.User} The authenticated User if valid otherwise an error.
 */
export async function thirdPartyAuth(
  temporaryCode,
  Parse,
  axios,
  Octokit,
  enterpriseServer36Admin,
) {
  return axios.post(
    configuration.oauthAccessTokenURL,
    {
      client_id: configuration.clientId,
      client_secret: configuration.clientSecret,
      code: temporaryCode,
    },
    {
      headers: {
        Accept: 'application/json',
      },
    },
  ).then(
    (response) => apiRequest(response.data.access_token, Parse, Octokit, enterpriseServer36Admin),
  );
}
