import { loadConfiguration } from 'src/services/ConfigurationService.js';
import Configuration from 'src/models/Configuration.js';
import User from 'src/entities/User';
import Group from 'src/entities/Group';
import EnterpriseGithub from 'src/auth_modules/enterpriseGithub.js';

describe('Test service: ConfigurationService', () => {
  describe('Test function: loadConfiguration', () => {
    it('should return instanciate Configuration object', () => {
      expect(loadConfiguration({ NODE_ENV: 'test' })).toEqual(new Configuration({
        mode: 'test',
        parseServer: {
          entities: [User, Group],
          auth: {
            github: {
              enabled: true,
            },
            enterpriseGithub: {
              module: new EnterpriseGithub(process.env.OAUTH_APP_API_BASE_URL),
            },
          },
        },
      }));
    });
  });
});
