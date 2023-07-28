/*
 * For a detailed explanation regarding each configuration property, visit:
 * https://jestjs.io/docs/configuration
 */

module.exports = {
  globals: {
    __DEV__: true,
  },
  testEnvironment: 'jsdom',
  coveragePathIgnorePatterns: ['/node_modules/'],
  testMatch: [
    '<rootDir>/tests/unit/**/*.spec.js',
  ],
  moduleFileExtensions: ['js'],
  moduleNameMapper: {
    '^src/(.*)$': '<rootDir>/src/$1',
    '^tests/(.*)$': '<rootDir>/tests/$1'
  },
  transformIgnorePatterns: ['<rootDir>/node_modules/'],
  testResultsProcessor: 'jest-sonar-reporter',
  collectCoverage: true,
  collectCoverageFrom: ['src/**/*.js'],
  coverageReporters: ['lcov', 'cobertura', 'text-summary', 'text'],
  coverageDirectory: './reports',
};
