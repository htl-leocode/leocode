FROM node:16-alpine3.14
WORKDIR ./frontend
COPY . .
RUN npm ci --legacy-peer-deps && npm run build # used to be npm ci

