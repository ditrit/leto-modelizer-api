import dotenv from 'dotenv';
import Configuration from '../models/Configuration.js';
import User from '../entities/User.js';
import Group from '../entities/Group.js';

/**
 * Load and return configuration.
 * @returns {Configuration} Initialized configuration.
 */
export function loadConfiguration() {
  dotenv.config({ path: `${process.env.NODE_ENV}.env` });
  const isEnterpriseGitHub = process.env.GITHUB_OAUTH_TYPE === 'enterprise';
  return new Configuration({
    mode: process.env.NODE_ENV,
    mountPath: process.env.PARSE_MOUNT,
    port: process.env.PORT,
    isEnterpriseGitHub,
    oauthAccessTokenURL: process.env.OAUTH_APP_ACCESS_TOKEN_URL,
    clientId: process.env.OAUTH_APP_CLIENT_ID,
    clientSecret: process.env.OAUTH_APP_CLIENT_SECRET,
    identityRequestEndpoint: process.env.OAUTH_IDENTITY_REQUEST_ENDPOINT,
    baseURL: process.env.OAUTH_APP_API_BASE_URL,
    parseServer: {
      databaseURI: process.env.DATABASE_URI,
      appId: process.env.APP_ID,
      masterKey: process.env.MASTER_KEY,
      masterKeyIps: process.env.MASTER_KEY_IPS,
      serverURL: process.env.SERVER_URL,
      liveQuery: {
        classNames: ['Posts', 'Comments'], // List of classes to support for query subscriptions
      },
      entities: [User, Group],
      auth: {
        github: {
          enabled: true,
        },
      },
    },
  });
}
