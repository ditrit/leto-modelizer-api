import { Then, When, Given } from '@badeball/cypress-cucumber-preprocessor';
import nunjucks from 'nunjucks';

/**
 * Make a request through cypress.
 * @param {string} endpoint - Endpoint to call.
 * @param {object} body - Body to pass via HTTP request
 * @param {string} method - HTTP method to use.
 * @param {object} headers - Headers for HTTP request
 * @param {boolean} isForm - Whether to convert the body values to URL encoded content.
 * @returns {Cypress.Chainable<void>} Cypress chainable promise.
 */
function request(endpoint, body, method = 'GET', headers = {}, isForm = false) {
  return cy.request({
    method,
    url: `http://localhost:1337${endpoint}`,
    body,
    headers: {
      'X-Parse-Application-Id': 'leto-modelizer-api-dev',
      'Content-Type': 'application/json',
      ...headers,
    },
    isForm,
    failOnStatusCode: false,
  })
    .then((response) => {
      cy.context.statusCode = response.status;
      cy.context.body = response.body;
    });
}

When('I request {string} with method {string} with masterKey', (endpointTemplate, method) => {
  const endpoint = nunjucks.renderString(endpointTemplate, cy.context);
  const headers = {
    'X-Parse-Master-Key': 'password',
  };
  request(endpoint, {}, method, headers);
});

When('I request {string}', (endpointTemplate) => {
  const endpoint = nunjucks.renderString(endpointTemplate, cy.context);

  request(endpoint);
});

When('I request {string} with method {string} and body with masterKey', (endpointTemplate, method, dataTable) => {
  const endpoint = nunjucks.renderString(endpointTemplate, cy.context);
  const body = {};
  const tables = dataTable.hashes();
  for (let i = 0; i < tables.length; i += 1) {
    body[tables[i].key] = tables[i].value;
  }
  const headers = {
    'X-Parse-Master-Key': 'password',
  };
  request(endpoint, body, method, headers);
});

When('I request {string} with method {string} with body as json with masterKey', (endpointTemplate, method, bodyTemplate) => {
  const body = JSON.parse(nunjucks.renderString(bodyTemplate.toString(), cy.context));
  const endpoint = nunjucks.renderString(endpointTemplate, cy.context);
  const headers = {
    'X-Parse-Master-Key': 'password',
  };

  request(endpoint, body, method, headers);
});

When('I request {string} with method {string} with query parameter(s) with masterKey', (endpointTemplate, method, dataTable) => {
  let endpoint = nunjucks.renderString(endpointTemplate, cy.context);
  const headers = {
    'X-Parse-Master-Key': 'password',
  };
  const queryParameters = dataTable.hashes();

  for (let index = 0; index < queryParameters.length; index += 1) {
    const value = nunjucks.renderString(queryParameters[index].value, cy.context);

    endpoint += index === 0 ? '?' : '&';
    endpoint += `${queryParameters[index].key}=${encodeURIComponent(value)}`;
  }

  request(endpoint, null, method, headers, true);
});

When('I request {string} with method {string} and body', (endpointTemplate, method, dataTable) => {
  const endpoint = nunjucks.renderString(endpointTemplate, cy.context);
  const body = {};
  const tables = dataTable.hashes();
  for (let i = 0; i < tables.length; i += 1) {
    body[tables[i].key] = tables[i].value;
  }
  request(endpoint, body, method);
});

Then('I expect {int} as status code', (status) => {
  expect(cy.context.statusCode).to.eq(status);
});

Then('I expect body is {string}', (bodyTemplate) => {
  const body = nunjucks.renderString(bodyTemplate, cy.context);

  expect(cy.context.body).to.eq(body);
});

Then('I expect body contains {int} attribute(s)', (length) => {
  expect(Object.keys(cy.context.body).length).to.eq(length);
});

Then('I expect body field {string} is {string}', (key, valueTemplate) => {
  const value = nunjucks.renderString(valueTemplate, cy.context);

  expect(cy.context.body[key]).to.eq(value);
});

Then('I expect length of array body field {string} is {int}', (key, length) => {
  expect(cy.context.body[key].length).to.eq(length);
});

Then('I set body field {string} to context field {string}', (key, contextField) => {
  cy.context[contextField] = cy.context.body[key];
});

Given('I purge role {string}', (roleName) => cy.request({
  method: 'GET',
  url: 'http://localhost:1337/api/roles',
  headers: {
    'X-Parse-Application-Id': 'leto-modelizer-api-dev',
    'X-Parse-Master-Key': 'password',
  },
  body: {
    where: { name: roleName },
  },
  isForm: false,
  failOnStatusCode: false,
})
  .then(async (response) => {
    if (response.body.results.length === 0) {
      return;
    }

    await cy.request({
      method: 'DELETE',
      url: `http://localhost:1337/api/roles/${response.body.results[0]?.objectId}`,
      headers: {
        'X-Parse-Application-Id': 'leto-modelizer-api-dev',
        'X-Parse-Master-Key': 'password',
      },
      isForm: false,
      failOnStatusCode: false,
    });
  }));

Given('I purge all users', () => request(
  '/api/purge/_User',
  {},
  'DELETE',
  {
    'X-Parse-Application-Id': 'leto-modelizer-api-dev',
    'X-Parse-Master-Key': 'password',
  },
));
