/**
 * This function is called by the Group entity beforeSave() Trigger.
 * It should throw an error if a new Group is saved with an unavailable name, else it returns true.
 * @param {boolean} isExistingObject - False if the request object is new.
 * @param {string} objectName - Name of the request object.
 * @param {string} sessionToken - SessionToken of the user making the request.
 * @param {object} Parse - The Parse object to use SDK functionalities.
 * @returns {Promise} Promise with nothing on success otherwise an error.
 */
export async function isGroupUnique(isExistingObject, objectName, sessionToken, Parse) {
  if (isExistingObject) {
    return Promise.resolve();
  }

  const query = new Parse.Query('Group');

  query.equalTo('name', objectName);

  return query.count({ sessionToken })
    .then((number) => {
      if (number > 0) {
        throw new Error('Group with this name already exists');
      }
    });
}
