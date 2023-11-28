/* eslint-disable */
// Parse should not be defined before use in this file -> eslint disabled to avoid lint error
/**
 * Cloud and trigger functions definition.
 */
import axios from 'axios';
import { Octokit } from 'octokit';
import * as enterpriseServer36Admin from '@octokit/plugin-enterprise-server';
import { thirdPartyAuth } from './cloud-functions.js';
import { importLibraryTemplates, fillLibraryFields, deleteAssociatedTemplates } from './library.js';
import { createAssociatedRole, deleteAssociatedRole } from './role.js';
import { isGroupUnique } from './group.js';

Parse.Cloud.beforeSave(
  'Group',
  (request) => isGroupUnique(
    request.object.existed(),
    request.object.get('name'),
    request.user?.getSessionToken(),
    Parse,
  ),
);

Parse.Cloud.define('GitHubAuth',
  (request) => {
    return thirdPartyAuth(
      request.params.code,
      Parse,
      axios,
      Octokit,
      enterpriseServer36Admin
    )
  }
);
Parse.Cloud.afterSave('Library',
  async (request) => {
    await importLibraryTemplates(
      request.object,
      axios,
      Parse
    );
    await createAssociatedRole(request.object, request.object.get('roleName'), Parse);
  }
);

Parse.Cloud.beforeSave('Library',
  async (request) => await fillLibraryFields(request, axios, Parse),
);

Parse.Cloud.beforeDelete(
  'Library',
  async (request) => await deleteAssociatedTemplates(request, Parse),
);

Parse.Cloud.afterDelete('Library',
  async (request) => await deleteAssociatedRole(request.object.get('roleName'), Parse),
);
