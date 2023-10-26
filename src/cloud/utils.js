/**
 * This function allows us to know if a field value is alredy used.
 * @param {object} request - The object from the trigger function that contains
 * the target object and the parameters.
 * @param {string} className - Name of the tested Class.
 * @param {string} fieldName - Name of the tested field.
 * @param {*} fieldValue - Value of the tested field.
 * @param {object} Parse - The Parse object to use SDK functionalities.
 * @returns {Promise} Promise with true on success, promise with false on failure.
 */
export async function isFieldAlreadyUsed(request, className, fieldName, fieldValue, Parse) {
  const query = new Parse.Query(className);
  query.equalTo(fieldName, fieldValue);

  // False if there is no object with this requested field
  // or if this field is owned by the updated object.
  return query.count({ useMasterKey: true })
    .then((number) => !(number === 0 || (number === 1 && request.object.existed())));
}
