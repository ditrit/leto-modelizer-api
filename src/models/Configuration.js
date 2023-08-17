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
   */
  constructor(props = {
    mode: null,
    mountPath: '/api',
    port: 1337,
    parseServer: {
      databaseURI: null,
      appId: 'leto-modelizer-api',
      masterKey: null,
      masterKeyIps: null,
      serverURL: 'http://localhost:1337/api',
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
     * LiveQuery configuration for parse server.
     * @type {object}
     */
    this.parseServer.liveQuery = {
      classNames: ['Posts', 'Comments'],
    };
  }
}

export default Configuration;
