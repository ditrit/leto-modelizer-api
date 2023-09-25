/* eslint-disable */
// Parse should not be defined before use in this file -> eslint disabled to avoid lint error
/**
 * Cloud and trigger functions definition.
 */
import { isGroupUnique } from './cloud-functions.js';

Parse.Cloud.beforeSave(
  'Group',
  (request) => isGroupUnique(
    request.object.existed(),
    request.object.get('name'),
    request.user?.getSessionToken(),
    Parse,
  ),
);
