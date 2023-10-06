import http from 'http';
import express from 'express';
import { ParseServer } from 'parse-server';
import { loadConfiguration } from './services/ConfigurationService.js';

const configuration = loadConfiguration();
const server = new ParseServer(configuration.parseServer);
const app = express();
const httpServer = http.createServer(app);

await server.start();
app.use(configuration.mountPath, server.app);

app.get('/', (req, res) => {
  res.status(200).send('Leto-modelizer-api: backend for leto-modelizer to manage libraries and access rights.');
});

app.get('/authenticationUrl', (req, res) => {
  if (configuration.isEnterpriseGitHub) {
    res.status(200).send(configuration.identityRequestEndpoint);
  } else {
    res.status(200).send(`https://github.com/login/oauth/authorize?client_id=${configuration.clientId}`);
  }
});

httpServer.listen(configuration.port, () => {
  console.log(`leto-modelizer-api running on port ${configuration.port}.`);
  console.log(`leto-modelizer-api running in mode ${configuration.mode}.`);
  if (configuration.isEnterpriseGitHub) {
    console.log('Github authentication type: Enterprise');
  } else {
    console.log('Github authentication type: Public');
  }
});

await ParseServer.createLiveQueryServer(httpServer);
