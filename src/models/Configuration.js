/**
 * All api configuration keys and values.
 */
class Configuration {
  /**
   * Default constructor.
   * @param {object} [props] - Object that contains all properties to set.
   * @param {string} [props.mode] - The name of your .env file and running mode.
   * @param {string} [props.mountPath] - That specifies the mount path where the Parse middleware
   * should be deployed in your Node.js application.
   * @param {number} [props.port] - The network port number on which a server process listens
   * for incoming client connections.
   * @param {boolean} [props.isEnterpriseGitHub] - Is GitHub authentication type enterprise.
   * @param {string} [props.oauthAccessTokenURL] - URL to request to get the User access_token.
   * @param {string} [props.clientId] - ClientId of the GitHub OAuth App.
   * @param {string} [props.clientSecret] - ClientSecret of the GitHub OAuth App.
   * @param {string} [props.identityRequestEndpoint] - The endpoint where the user authenticates
   * himself to the provider.
   * @param {string} [props.baseURL] - URL to request on the GitHub API.
   * @param {object} [props.parseServer] - Parse server configuration object.
   * @param {string} [props.parseServer.databaseURI] - Represents a connection string used to
   * connect to a database.
   * @param {string} [props.parseServer.appId] - A unique identifier for your application,
   * used to distinguish it when interacting with the Parse backend.
   * @param {string} [props.parseServer.masterKey] - A special access key that grants
   * unrestricted access to the Parse backend, allowing administrative-level operations.
   * and data management.
   * @param {string} [props.parseServer.masterKeyIps] - IPs allowed to use the master key.
   * @param {string} [props.parseServer.serverURL] - That defines the URL where the server
   * is hosted, enabling clients to connect and interact with the Parse backend.
   * @param {Array} [props.parseServer.entities] - Define the list of entities in the database.
   * @param {object} [props.parseServer.auth] - Define the supported 3rd Party
   * authentication methods.
   */
  constructor(props = {
    mode: null,
    mountPath: '/api',
    port: 1337,
    isEnterpriseGitHub: false,
    oauthAccessTokenURL: 'https://github.com/login/oauth/access_token',
    clientId: null,
    clientSecret: null,
    baseURL: null,
    identityRequestEndpoint: null,
    parseServer: {
      databaseURI: null,
      appId: 'leto-modelizer-api',
      masterKey: null,
      masterKeyIps: null,
      serverURL: 'http://localhost:1337/api',
      entities: [],
      auth: {},
    },
  }) {
    /**
     * The name of your .env file and running mode.
     * @type {string}
     */
    this.mode = props.mode || null;
    /**
     * That specifies the mount path where the Parse middleware should be deployed in your
     * Node.js application.
     * @type {string}
     */
    this.mountPath = props.mountPath || '/api';
    /**
     * The network port number on which a server process listens for incoming client connections.
     * @type {number}
     */
    this.port = props.port || 1337;
    /**
     * Is GitHub authentication type enterprise.
     * @type {boolean}
     */
    this.isEnterpriseGitHub = props.isEnterpriseGitHub || false;
    /**
     * URL to request to get the User access_token.
     * @type {string}
     */
    this.oauthAccessTokenURL = props.oauthAccessTokenURL;
    /**
     * ClientId of the GitHub OAuth App.
     * @type {string}
     */
    this.clientId = props.clientId;
    /**
     * ClientSecret of the GitHub OAuth App.
     * @type {string}
     */
    this.clientSecret = props.clientSecret;
    /**
     * The endpoint where the user authenticates himself to the provider.
     * @type {string}
     */
    this.identityRequestEndpoint = props.identityRequestEndpoint;
    /**
     * URL to request on the GitHub API.
     * @type {string}
     */
    this.baseURL = props.baseURL;
    /**
     * Parse server configuration object.
     * @type {object}
     */
    this.parseServer = {};
    /**
     * Represents a connection string used to connect to a database.
     * @type {string}
     */
    this.parseServer.databaseURI = props.parseServer?.databaseURI || null;
    /**
     * A unique identifier for your application, used to distinguish it when interacting with
     * the Parse backend.
     * @type {string}
     */
    this.parseServer.appId = props.parseServer?.appId || 'leto-modelizer-api';
    /**
     * A special access key that grants unrestricted access to the Parse backend, allowing
     * administrative-level operations and data management.
     * @type {string}
     */
    this.parseServer.masterKey = props.parseServer?.masterKey || null;
    /**
     * IPs allowed to use the master key.
     * @type {string[]}
     */
    this.parseServer.masterKeyIps = props.parseServer?.masterKeyIps?.split(',') || [];
    /**
     * That defines the URL where the server is hosted, enabling clients to connect and
     * interact with the Parse backend.
     * @type {string}
     */
    this.parseServer.serverURL = props.parseServer?.serverURL || 'http://localhost:1337/api';
    /**
     * The path to the file that contains the cloud functions.
     * @type {string}
     */
    this.parseServer.cloud = 'src/cloud/main.js';
    /**
     * LiveQuery configuration for parse server.
     * @type {object}
     */
    this.parseServer.liveQuery = {
      classNames: ['Posts', 'Comments'],
    };
    /**
     * Define a schema for the Parse Server.
     * @type {object}
     */
    this.parseServer.schema = {
      definitions: props.parseServer?.entities || [],
      // If set to true, schema changes are disabled and applying them is only possible
      // by redeploying Parse Server with a new schema definition.
      lockSchemas: true,
      // If set to `true`, Parse Server will automatically delete non-defined classes from
      // the database; internal classes like `User` or `Role` are never deleted.
      strict: true,
      // If set to `true`, a field type change will cause the field including its data to be
      // deleted from the database, and then a new field to be created with the new type.
      recreateModifiedFields: false,
      // If set to `true`, Parse Server will automatically delete non-defined class fields;
      // internal fields in classes like User or Role are never deleted.
      deleteExtraFields: false,
    };
    /**
     * Define the supported 3rd Party authentication methods.
     * @type {object}
     */
    this.parseServer.auth = props.parseServer?.auth || {};
  }
}

export default Configuration;
