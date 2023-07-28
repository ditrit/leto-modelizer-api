FROM node:18.14-alpine
ARG NODE_ENV
WORKDIR /app
COPY package*.json ./
RUN npm install -g npm@8.19.3 && npm install
COPY . .
EXPOSE 1337
ENV NODE_ENV=${NODE_ENV}
CMD ["npm", "run", "start"]
