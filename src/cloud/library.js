import { isFieldAlreadyUsed } from './utils.js';

/**
 * This function is called when a Library is deleted and delete the associated templates.
 * @param {object} request - The object from the trigger function that contains
 * the target object and the parameters.
 * @param {*} Parse - The Parse object to use SDK functionalities.
 */
export async function deleteAssociatedTemplates(request, Parse) {
  const Template = Parse.Object.extend('Template');
  const query = new Parse.Query(Template);

  await query.find({ useMasterKey: true }).then(async (result) => {
    result.forEach((i) => {
      const innerQuery = i.relation('library').query().equalTo('objectId', request.object.id);

      innerQuery.find({ useMasterKey: true }).then(async (r) => {
        i.relation('library').remove(r);

        await i.save(null, { useMasterKey: true });

        await i.destroy({ useMasterKey: true });
      });
    });
  });
}

/**
 * This function is called by the Library entity afterSave(),
 * it ensures that the associated _Role is created along with the Library related Templates.
 * @param {object} library - The Library instance to be saved.
 * @param {object} axios - To access axios request functionalities.
 * @param {object} Parse - The Parse object to use SDK functionalities.
 * @returns {Promise} A promise with nothing on success, otherwise an error.
 */
export async function importLibraryTemplates(library, axios, Parse) {
  const gitUrl = library.get('url');
  const rawIndex = await axios.get(gitUrl);

  // Create the related templates
  rawIndex.data.templates.forEach(async (element) => {
    const Template = Parse.Object.extend('Template');
    const template = new Template();
    template.set('key', element.key);
    template.set('name', element.name);
    template.set('plugin', element.plugin);
    template.set('type', element.type);
    template.set('description', element.description);
    template.set('url', element.url);
    template.set('files', element.files);
    template.set('schemas', element.schemas);
    template.set('plugins', element.plugins);
    template.relation('library').add(library);

    await template.save(null, { useMasterKey: true });
  });
  return Promise.resolve();
}

/**
 * This function is called by the beforeSave() trigger function for the class Library.
 * If the saved library is a new one, the associated Role is created also and the
 * uniqueness of the url is checked. If the library is an existing one that is being updated,
 * the associated templates are deleted.
 * @param {object} request - The object from the trigger function that contains
 * the target object and the parameters.
 * @param {object} axios - To access axios request functionalities.
 * @param {object} Parse - The Parse object to use SDK functionalities.
 * @returns {Promise} A promise with nothing on success, otherwise an error.
 */
export async function fillLibraryFields(request, axios, Parse) {
  const url = request.object.get('url');
  const roleName = request.object.get('roleName');
  const rawIndex = await axios.get(url);
  request.object.set('name', rawIndex.data.name);
  request.object.set('author', rawIndex.data.author);
  request.object.set('version', rawIndex.data.version);
  request.object.set('description', rawIndex.data.description);

  if (request.object.existed()) {
    /*
      If this library is updated I delete all related templates
      in order to recreate them in afterSave() trigger function.
      */
    await deleteAssociatedTemplates(request, Parse);
    return Promise.resolve();
  }
  // Check if the new Library is imported from an unused URL and an unused roleName.
  if (await isFieldAlreadyUsed(request, 'Library', 'url', url, Parse)) {
    throw new Error('Library with this url already exists');
  }
  if (await isFieldAlreadyUsed(request, 'Library', 'roleName', roleName, Parse)) {
    throw new Error('Library with this roleName already exists');
  }
  if (/[^a-z0-9_]/.test(roleName)) {
    throw new Error('roleName must contain only lower case alphanumerics and underscores');
  }
  return Promise.resolve();
}
