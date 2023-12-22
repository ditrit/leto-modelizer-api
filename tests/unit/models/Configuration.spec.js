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
      expect(configuration.parseServer.masterKeyIps).toEqual([]);
      expect(configuration.parseServer.serverURL).toEqual('http://localhost:1337/api');
      expect(configuration.parseServer.schema).toEqual({
        definitions: [],
        deleteExtraFields: false,
        lockSchemas: true,
        recreateModifiedFields: false,
        strict: true,
      });
      expect(configuration.parseServer.auth).toEqual({});
      expect(configuration.parseServer.domainWhitelist).toEqual(['']);
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
      expect(configuration.parseServer.masterKeyIps).toEqual([]);
      expect(configuration.parseServer.serverURL).toEqual('http://localhost:1337/api');
      expect(configuration.parseServer.schema).toEqual({
        definitions: [],
        deleteExtraFields: false,
        lockSchemas: true,
        recreateModifiedFields: false,
        strict: true,
      });
      expect(configuration.parseServer.auth).toEqual({});
      expect(configuration.parseServer.domainWhitelist).toEqual([]);
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
          masterKeyIps: '1.1.0.0/0',
          serverURL: 'url',
          entities: [],
          auth: {
            myModule: true,
          },
          domainWhitelist: 'http://templates/,https://templates/',
        },
      });

      expect(configuration.mode).toEqual('dev');
      expect(configuration.mountPath).toEqual('/test');
      expect(configuration.port).toEqual(1338);
      expect(configuration.parseServer).not.toBeNull();
      expect(configuration.parseServer.databaseURI).toEqual('test2');
      expect(configuration.parseServer.appId).toEqual('appId');
      expect(configuration.parseServer.masterKey).toEqual('master');
      expect(configuration.parseServer.masterKeyIps).toEqual(['1.1.0.0/0']);
      expect(configuration.parseServer.serverURL).toEqual('url');
      expect(configuration.parseServer.schema).toEqual({
        definitions: [],
        deleteExtraFields: false,
        lockSchemas: true,
        recreateModifiedFields: false,
        strict: true,
      });
      expect(configuration.parseServer.auth).toEqual({ myModule: true });
      expect(configuration.parseServer.domainWhitelist).toEqual(['http://templates/', 'https://templates/']);
    });
  });
});
