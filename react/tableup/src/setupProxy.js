
const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
  app.use(
    //리액트에서 /api/test로 요청하면 스프링에서는 /test로 맵핑
    '/api', 
    createProxyMiddleware({
      target: 'http://localhost:8080', 
      changeOrigin: true,
      pathRewrite: {
        '^/api': '', // /api로 시작하는 경로를 제거
      },
    })
  );
};
