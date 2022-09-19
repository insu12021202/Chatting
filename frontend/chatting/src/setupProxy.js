/*
 * https://create-react-app.dev/docs/proxying-api-requests-in-development
 *
 * 프록시 설정을 해주면 CORS 에러 문제를 해결할 수 있다.
 *
 * Note: You do not need to import this file anywhere.
 * It is automatically registered when you start the development server.
 * 다른 곳에 임포트해서 사용할 필요 없이 자동으로 적용된다.
 *
 * 만약  /api/hello 이런식으로 사용하면 자동으로 localhost:5000 으로 설정된다
 */

// -------------------------------------------------------------

const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = function (app) {
  app.use(
    "ws",
    createProxyMiddleware({
      target: "http://localhost:8080",
      ws: true,
    })
  );
};
