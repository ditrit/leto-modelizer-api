import EnterpriseGithub from 'src/auth_modules/enterpriseGithub.js';

describe('Test function: validateAppId', () => {
  it('should return a resolved promise when try to save a group that already exists', async () => {
    await expect(new EnterpriseGithub(process.env.OAUTH_APP_API_BASE_URL).validateAppId())
      .resolves
      .toBeUndefined();
  });
});

describe('Test function: validateAuthData', () => {
  it('should throw an error when try to validate enterprise-github data outside enterprise environnement', async () => {
    const authData = {
      access_token: 'access',
      id: 'id',
    };
    await expect(
      new EnterpriseGithub(process.env.OAUTH_APP_API_BASE_URL).validateAuthData(authData),
    )
      .rejects
      .toThrow('Request cannot be performed, check that you have access to the given address');
  });
});
