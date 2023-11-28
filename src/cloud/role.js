/**
 *This function is called when an object is saved and creates the role associated at this object.
 * @param {object} object - The object from the trigger function that
 * is being update, created or deleted.
 * @param {string} roleName - The name of the role to create.
 * @param {*} Parse - The Parse object to use SDK functionalities.
 */
export async function createAssociatedRole(object, roleName, Parse) {
  if (!object.existed()) {
    // Create an ACL for the new library_user Role
    const roleAcl = new Parse.ACL();
    roleAcl.setPublicReadAccess(true);

    // Create the role "roleName_user" for this new object
    const objectUserRole = new Parse.Role(roleName, roleAcl);
    try {
      await objectUserRole.save(null, { useMasterKey: true });

      const objectAcl = new Parse.ACL();

      objectAcl.setReadAccess(objectUserRole, true);
      objectAcl.setWriteAccess(await new Parse.Query(Parse.Role).equalTo('name', 'admin').first({ useMasterKey: true }), true);

      object.setACL(objectAcl);
    } catch (error) {
      console.error(error.message);
    }
  }
}

/**
 * This function is called when an object is deleted and deletes
 * the role associated at this object.
 * @param {string} roleName - The name of the role to delete.
 * @param {*} Parse - The Parse object to use SDK functionalities.
 */
export async function deleteAssociatedRole(roleName, Parse) {
  const query = new Parse.Query(Parse.Role);
  query.equalTo('name', roleName);

  const associatedRole = await query.first({ useMasterKey: true });

  await associatedRole.destroy({ useMasterKey: true });
}
