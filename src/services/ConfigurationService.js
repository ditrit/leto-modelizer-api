import dotenv from 'dotenv';
import Configuration from '../models/Configuration.js';
import User from '../entities/User.js';

/**
 * Load and return configuration.
 * @returns {Configuration} Initialized configuration.
 */
export function loadConfiguration() {
  dotenv.config({ path: `${process.env.NODE_ENV}.env` });
  return new Configuration({
    mode: process.env.NODE_ENV,
    mountPath: process.env.PARSE_MOUNT,
    port: process.env.PORT,
    parseServer: {
      databaseURI: process.env.DATABASE_URI,
      appId: process.env.APP_ID,
      masterKey: process.env.MASTER_KEY,
      masterKeyIps: process.env.MASTER_KEY_IPS,
      serverURL: process.env.SERVER_URL,
      liveQuery: {
        classNames: ['Posts', 'Comments'], // List of classes to support for query subscriptions
      },
      entities: [User],
    },
  });
}
