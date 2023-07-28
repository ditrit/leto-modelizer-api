import Configuration from 'src/models/Configuration.js';

describe('Test class: Configuration', () => {
  describe('Test constructor', () => {
    it('Checks variables instantiation', () => {
      const configuration = new Configuration();

      expect(configuration.mode).toBeNull();
      expect(configuration.mountPath).toEqual('/api');
      expect(configuration.port).toEqual(1337);
      expect(configuration.parseServer).not.toBeNull();
      expect(configuration.parseServer.databaseURI).toBeNull();
      expect(configuration.parseServer.appId).toEqual('leto-modelizer-api');
      expect(configuration.parseServer.masterKey).toBeNull();
      expect(configuration.parseServer.serverURL).toEqual('http://localhost:1337/api');
    });

    it('Checks passing undefined variables to constructor', () => {
      const configuration = new Configuration({});

      expect(configuration.mode).toBeNull();
      expect(configuration.mountPath).toEqual('/api');
      expect(configuration.port).toEqual(1337);
      expect(configuration.parseServer).not.toBeNull();
      expect(configuration.parseServer.databaseURI).toBeNull();
      expect(configuration.parseServer.appId).toEqual('leto-modelizer-api');
      expect(configuration.parseServer.masterKey).toBeNull();
      expect(configuration.parseServer.serverURL).toEqual('http://localhost:1337/api');
    });

    it('Checks passing all variables to constructor', () => {
      const configuration = new Configuration({
        mode: 'dev',
        mountPath: '/test',
        port: 1338,
        parseServer: {
          databaseURI: 'test2',
          appId: 'appId',
          masterKey: 'master',
          serverURL: 'url',
        },
      });

      expect(configuration.mode).toEqual('dev');
      expect(configuration.mountPath).toEqual('/test');
      expect(configuration.port).toEqual(1338);
      expect(configuration.parseServer).not.toBeNull();
      expect(configuration.parseServer.databaseURI).toEqual('test2');
      expect(configuration.parseServer.appId).toEqual('appId');
      expect(configuration.parseServer.masterKey).toEqual('master');
      expect(configuration.parseServer.serverURL).toEqual('url');
    });
  });
});
