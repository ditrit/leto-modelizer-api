#! /usr/bin/env node
import axios from 'axios';
import chalk from 'chalk';
import { Command } from 'commander';
import dotenv from 'dotenv';
import prompts from 'prompts';
import { readFileSync } from 'fs';

const program = new Command();
const configuration = {
  username: null,
  configFile: null,
  apiUrl: null,
  appId: null,
  masterKey: null,
};
const authorizedOptions = ['username', 'configFile', 'apiUrl'];

/**
 * Get Axios configuration with headers.
 * @returns {object} Axios configuration with headers.
 */
function getAxiosConfiguration() {
  return {
    headers: {
      Accept: 'application/json',
      'X-Parse-Application-Id': configuration.appId,
      'X-Parse-Master-Key': configuration.masterKey,
    },
  };
}

/**
 * Get user from api.
 * @returns {Promise<object>} Promise with user object on success otherwise an error.
 */
async function getUser() {
  return axios.get(`${configuration.apiUrl}/users?where={"username":"${configuration.username}"}`, getAxiosConfiguration())
    .then((response) => response.data.results[0]);
}

/**
 * Get admin role from api.
 * @returns {Promise<object>} Promise with admin role object on success otherwise
 * an error.
 */
async function getAdminRole() {
  return axios.get(`${configuration.apiUrl}/roles?where={"name":"admin"}`, getAxiosConfiguration())
    .then((response) => response.data.results[0]);
}

/**
 * Add role on user.
 * @param {string} roleId - Role id.
 * @param {string} userId - User id.
 * @returns {Promise<axios.AxiosResponse<any>>} Promise with axios response on success otherwise
 * an error.
 */
async function addRoleToUser(roleId, userId) {
  return axios.put(`${configuration.apiUrl}/roles/${roleId}`, {
    users: {
      __op: 'AddRelation',
      objects: [
        {
          __type: 'Pointer',
          className: '_User',
          objectId: userId,
        },
      ],
    },
  }, getAxiosConfiguration());
}

/**
 * Prompt the user for the configuration file location or retrieve it from the existing
 * configuration.
 * @returns {Promise<object>} Promise with configuration file location on success otherwise
 * an error.
 */
async function setupConfigFile() {
  if (configuration.configFile) {
    console.log(`\n${chalk.blue('ⓘ')} Configuration to use: ${configuration.configFile}`);

    return configuration.configFile;
  }

  const { config } = await prompts({
    type: 'text',
    name: 'config',
    message: 'Location of api configuration?',
    initial: './dev.env',
  });

  return config;
}

/**
 * Prompt the user for the GitHub username or retrieve it from the existing configuration.
 * @returns {Promise<object>} Promise with GitHub username on success otherwise an error.
 */
async function setupUsername() {
  if (configuration.username) {
    console.log(`\n${chalk.blue('ⓘ')} Github username to use: ${configuration.username}`);

    return configuration.username;
  }

  const { username } = await prompts({
    type: 'text',
    name: 'username',
    message: 'What is the github username?',

  });

  return username;
}

/**
 * Prompt the user for the api url or retrieve it from the existing configuration.
 * @returns {Promise<object>} Promise with api url on success otherwise an error.
 */
async function setupApiUrl() {
  if (configuration.apiUrl) {
    console.log(`\n${chalk.blue('ⓘ')} Api url to use: ${configuration.apiUrl}`);

    return configuration.apiUrl;
  }

  const { apiUrl } = await prompts({
    type: 'text',
    name: 'apiUrl',
    message: 'Url of your api?',
    initial: 'http://localhost:1337/api',
  });

  return apiUrl;
}

program.command('init')
  .description('Install plugin in leto-modelizer')
  .action(async () => {
    process.argv.slice(3).forEach((option) => {
      const [, key, value] = /^([^=]+)=([^=]+)$/.exec(option);

      if (authorizedOptions.includes(key)) {
        configuration[key] = value;
      }
    });

    configuration.configFile = await setupConfigFile();
    configuration.apiUrl = await setupApiUrl();
    configuration.username = await setupUsername();

    const env = dotenv.parse(readFileSync(configuration.configFile));

    configuration.masterKey = env.MASTER_KEY;
    configuration.appId = env.APP_ID;

    console.log(`\n${chalk.blue.bold('⚒')} Search user on api...`);

    const user = await getUser();

    if (!user) {
      console.log(`\n${chalk.red('✘')} No user found with username: ${configuration.username}`);

      return;
    }

    console.log(`\n${chalk.green('✔')} User found !`);

    console.log(`\n${chalk.blue.bold('⚒')} Search admin role on api...`);

    const role = await getAdminRole();

    if (!role) {
      console.log(`\n${chalk.red('✘')} Admin role not found.`);

      return;
    }

    console.log(`\n${chalk.green('✔')} Admin role found !`);

    console.log(`\n${chalk.blue.bold('⚒')} Try to attach admin role on user...`);

    await addRoleToUser(role.objectId, user.objectId);

    console.log(`\n${chalk.green('✔')} User '${configuration.username}' is now an admin!`);
  });

program.parse();
