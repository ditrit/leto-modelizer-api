const { defineConfig } = require('cypress');
const preprocessor = require('@badeball/cypress-cucumber-preprocessor');
const browserify = require('@badeball/cypress-cucumber-preprocessor/browserify');

async function setupNodeEvents(on, config) {
  await preprocessor.addCucumberPreprocessorPlugin(on, config);

  on('file:preprocessor', browserify.default(config));

  // Make sure to return the config object as it might have been modified by the plugin.
  return config;
}

module.exports = defineConfig({
  e2e: {
    baseUrl: 'http://localhost:1337/api/health',
    specPattern: 'tests/e2e/**/*.feature',
    supportFile: './tests/e2e/support/e2e.js',
    video: false,
    screenshotOnRunFailure: false,
    setupNodeEvents,
    experimentalRunAllSpecs: true,
  },
  retries: 3,
});
