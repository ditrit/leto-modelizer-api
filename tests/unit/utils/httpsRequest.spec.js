import httpsRequest from 'src/utils/httpsRequest.js';

describe('Test function: get', () => {
  it('should return a resolved promise', async () => {
    const orgObject = await httpsRequest.get({
      host: 'api.github.com',
      path: '/orgs/ditrit',
      headers: {
        'User-Agent': 'parse-server',
      },
    });
    await expect(orgObject.blog).toEqual('http://ditrit.io');
  });
  it('should return an empty string when requiring github auth without parameters', async () => {
    const authResp = await httpsRequest.get('https://github.com/login/oauth/authorize', true);
    await expect(authResp).toEqual('');
  });
  it('should reject when request with non JSON options', async () => {
    try {
      await httpsRequest.get('https://github.com/login/oauth/authorize');
    } catch (error) {
      expect(error.message).toEqual('Unexpected end of JSON input');
    }
  });
  it('should reject when request on non-valid url', async () => {
    try {
      await httpsRequest.get({
        host: 'not.valid',
        path: 'path',
        headers: {
          'User-Agent': 'parse-server',
        },
      });
    } catch (error) {
      expect(error.message).toEqual('getaddrinfo ENOTFOUND not.valid');
    }
  });
});

describe('Test function: request', () => {
  it('should return a resolved promise', async () => {
    const options = {
      host: 'api.github.com',
      path: '/orgs/ditrit',
      method: 'GET',
      headers: {
        'User-Agent': 'parse-server',
      },
    };
    const orgObject = await httpsRequest.request(options, 'data');
    await expect(orgObject.blog).toEqual('http://ditrit.io');
  });
});
