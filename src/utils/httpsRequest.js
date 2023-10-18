// This is a file from parse-server documentation and is used for the custom authentication modules.
// Please see https://docs.parseplatform.org/parse-server/guide/#custom-authentication for more information.

import https from 'https';

/**
 * Callback function for get and request methods
 * @param {Function} resolve - Resolves the Promise where this function is called.
 * @param {Function} reject - Rejects the Promise where this function is called.
 * @param {boolean} noJSON - Wether the options in the request should be JSON or not.
 * @returns {Promise} Promise with nothing on success otherwise an error.
 */
function makeCallback(resolve, reject, noJSON) {
  return (response) => {
    let data = '';

    response.on('data', (chunk) => {
      data += chunk;
    });

    response.on('end', () => {
      if (noJSON) {
        return resolve(data);
      }
      try {
        data = JSON.parse(data);
      } catch (e) {
        return reject(e);
      }
      return resolve(data);
    });

    response.on('error', reject);
  };
}

/**
 * A function to perfom a https GET with callback.
 * @param {*} options - JSON with request options or only the URL.
 * @param {boolean} noJSON - Wether the options in the request should be JSON or not.
 * @returns {Promise} Promise with nothing on success otherwise an error.
 */
function get(options, noJSON = false) {
  return new Promise((resolve, reject) => {
    https.get(options, makeCallback(resolve, reject, noJSON)).on('error', reject);
  });
}

/**
 * A function to perfom a https request with callback.
 * @param {*} options - JSON with request options or only the URL.
 * @param {object} postData - data to pass when the method is POST.
 * @returns {Promise} Promise with nothing on success otherwise an error.
 */
function request(options, postData) {
  return new Promise((resolve, reject) => {
    const httpsRequest = https.request(options, makeCallback(resolve, reject));

    httpsRequest.on('error', reject);
    httpsRequest.write(postData);
    httpsRequest.end();
  });
}

export default { get, request };
