module.exports = {
  root: true,

  parserOptions: {
    parser: '@babel/eslint-parser',
    ecmaVersion: 2022,
    sourceType: 'module'
  },

  env: {
    jest: true,
  },

  extends: [
    'airbnb-base',
    'plugin:cypress/recommended',
    'plugin:jsdoc/recommended',
  ],

  rules: {
    'linebreak-style': ['error', 'unix'],

    'no-param-reassign': 'off',
    'no-void': 'off',
    'no-nested-ternary': 'off',
    'no-console': 'off',
    'no-underscore-dangle': 'off',
    'max-classes-per-file': 'off',

    'import/first': 'off',
    'import/named': 'error',
    'import/namespace': 'error',
    'import/default': 'error',
    'import/export': 'error',
    'import/extensions': 'off',
    'import/no-unresolved': 'off',
    'import/no-extraneous-dependencies': 'off',
    'import/prefer-default-export': 'off',

    'prefer-promise-reject-errors': 'off',

    'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    'jsdoc/no-undefined-types': 'off',
  }
}
