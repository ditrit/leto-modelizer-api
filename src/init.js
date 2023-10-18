/**
 * Initialize Parse in order to use SDK functionalities.
 * @param {object} configuration - The api configuration.
 * @param {*} Parse - The Parse object to use SDK functionalities.
 * @returns {Promise} A resolved promise with nothing on success otherwise an error.
 */
async function initParse(configuration, Parse) {
  Parse.initialize(configuration.parseServer.appId, 'JAVASCRIPT_KEY', configuration.parseServer.masterKey);
  Parse.serverURL = configuration.parseServer.serverURL;
}

/**
 * Create a new role if it does not already exist.
 * @param {string} roleName - The name of the role to be created
 * @param {*} Parse - The Parse object to use SDK functionalities.
 * @returns {boolean} True if the new role has been created, false otherwise.
 */
async function initRole(roleName, Parse) {
  const role = new Parse.Role();

  // Check if the role already exists
  let roleExists = null;

  try {
    const query = new Parse.Query(role);
    query.equalTo('name', roleName);

    roleExists = await query.first({ useMasterKey: true });
  } catch (error) {
    console.error(`Cannot query roles during backend initalization : ${error.message}`);

    return false;
  }

  // If the role already exists, we have nothing to do here
  if (roleExists) {
    console.log('Role "admin" already exists');

    return false;
  }

  // If the role does not exist, create it and set the ACLs
  const acl = new Parse.ACL();
  acl.setPublicReadAccess(true);
  acl.setPublicWriteAccess(false);

  const newRole = new Parse.Role();
  newRole.set('name', roleName);
  newRole.setACL(acl);

  try {
    await newRole.save({}, { useMasterKey: true });
    console.log(`new ${roleName} role created`);
  } catch (error) {
    console.error(`Error during admin role initialization : ${error.message}`);

    return false;
  }
  return true;
}

/**
 * Setup every default roles needed at the initialization of the server.
 * @param {object} configuration - The server configuration.
 * @param {*} Parse - The Parse object to use SDK functionalities.
 * @returns {Promise} Promise with true on success otherwise promise with false.
 */
export async function setupDefaultRoles(configuration, Parse) {
  await initParse(configuration, Parse);
  console.log('init parse done');

  return initRole('admin', Parse);
}
