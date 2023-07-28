import dotenv from 'dotenv';
import Configuration from '../models/Configuration.js';

/**
 * Load and return configuration.
 * @returns {Configuration} Initialized configuration.
 */
export function loadConfiguration() {
  dotenv.config({ path: `${process.env.NODE_ENV}.env` });

  return new Configuration({
    mode: process.env.NODE_ENV,
    mountPath: process.env.PARSE_MOUNT || '/api',
    port: process.env.PORT || 1337,
    parseServer: {
      databaseURI: process.env.DATABASE_URI || '', // ,
      appId: process.env.APP_ID || 'leto-modelizer-api',
      masterKey: process.env.MASTER_KEY || '',
      serverURL: process.env.SERVER_URL || 'http://localhost:1337/api',
      liveQuery: {
        classNames: ['Posts', 'Comments'], // List of classes to support for query subscriptions
      },
    },
  });
}
