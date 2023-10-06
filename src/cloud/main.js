/* eslint-disable */
// Parse should not be defined before use in this file -> eslint disabled to avoid lint error
/**
 * Cloud and trigger functions definition.
 */
import axios from 'axios';
import { Octokit } from 'octokit';
import * as enterpriseServer36Admin from '@octokit/plugin-enterprise-server';
import { isGroupUnique, thirdPartyAuth } from './cloud-functions.js';

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
