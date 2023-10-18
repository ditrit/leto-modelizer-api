import { Then, When } from '@badeball/cypress-cucumber-preprocessor';
import nunjucks from 'nunjucks';

/**
 * Make a request through cypress.
 * @param {string} endpoint - Endpoint to call.
 * @param {object} body - Body to pass via HTTP request
 * @param {string} method - HTTP method to use.
 * @param {object} headers - Headers for HTTP request
 * @returns {Cypress.Chainable<void>} Cypress chainable promise.
 */
function request(endpoint, body, method = 'GET', headers = { 'X-Parse-Application-Id': 'leto-modelizer-api-dev', 'Content-Type': 'application/json' }) {
  return cy.request({
    method,
    url: `http://localhost:1337${endpoint}`,
    body,
    headers,
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
    'X-Parse-Application-Id': 'leto-modelizer-api-dev',
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
    'X-Parse-Application-Id': 'leto-modelizer-api-dev',
    'X-Parse-Master-Key': 'password',
  };
  request(endpoint, body, method, headers);
});

When('I request {string} with method {string} with body as json and masterKey', (endpointTemplate, method, body) => {
  const endpoint = nunjucks.renderString(endpointTemplate, cy.context);
  const headers = {
    'X-Parse-Application-Id': 'leto-modelizer-api-dev',
    'X-Parse-Master-Key': 'password',
  };
  request(endpoint, body, method, headers);
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
