import { Then, When } from '@badeball/cypress-cucumber-preprocessor';
import nunjucks from 'nunjucks';

/**
 * Make a request through cypress.
 * @param {string} endpoint - Endpoint to call.
 * @param {string} method - HTTP method to use.
 * @returns {Cypress.Chainable<void>} Cypress chainable promise.
 */
function request(endpoint, method = 'GET') {
  return cy.request({
    method,
    url: `http://localhost:1337/api${endpoint}`,
    failOnStatusCode: false,
  })
    .then((response) => {
      cy.context.statusCode = response.status;
      cy.context.body = response.body;
    });
}

When('I request {string}', (endpointTemplate) => {
  const endpoint = nunjucks.renderString(endpointTemplate, cy.context);

  request(endpoint);
});

Then('I expect {int} as status code', (status) => {
  expect(cy.context.statusCode).to.eq(status);
});

Then('I expect body contains {int} attribute(s)', (length) => {
  expect(Object.keys(cy.context.body).length).to.eq(length);
});

Then('I expect body field {string} is {string}', (key, valueTemplate) => {
  const value = nunjucks.renderString(valueTemplate, cy.context);

  expect(cy.context.body[key]).to.eq(value);
});
