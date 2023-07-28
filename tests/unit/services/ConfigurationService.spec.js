import { loadConfiguration } from 'src/services/ConfigurationService.js';
import Configuration from 'src/models/Configuration.js';

describe('Test service: ConfigurationService', () => {
  describe('Test function: loadConfiguration', () => {
    it('should return instanciate Configuration object', () => {
      expect(loadConfiguration()).toEqual(new Configuration({ mode: 'test' }));
    });
  });
});
