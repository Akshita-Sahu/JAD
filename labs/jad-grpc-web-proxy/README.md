# JAD-grpc
:

## 1. grpc-web
1. grpc-webipport: [](./ui/src/main.js)
    ```js
      app.use(ViewUIPlus)
        .use(router)
        .provide("apiHost","http://localhost:8567")
        .mount('#app')
    ```
2. : [](./src/main/java/com/akshita-sahu/jad/grpcweb/grpc/DemoBootstrap.java), ``GRPC_WEB_PROXY_PORT``,grpc-web。<br><br>
   grpchttp, `GRPC_PORT``HTTP_PORT`<br><br>
  *, grpc-web(: 8567) 
## 2. 

```shell
mvn compile
```

## 3. 

 [com.akshita.jad.grpcweb.grpc.DemoBootstrap](./src/main/java/com/akshita-sahu/jad/grpcweb/grpc/DemoBootstrap.java)

## 4. 
,
```text
Open your web browser and navigate to http://127.0.0.1:{http_port}/index.html
```

# netty grpc web proxy

grpc-web

from: https://github.com/grpc/grpc-web/tree/1.4.2/src/connector

， netty 。

## 

 `.proto`  `.class`，`GreeterGrpc`，。


## 

IDE,test

 com.akshita.jad.grpcweb.proxy.server.GrpcWebProxyServerTest 



* https://github.com/grpc/grpc-web/

## 

 grpc web proxy。

##  envoy

envoy ，`envoy.yaml`

* `envoy --config-path ./envoy.yaml`

##  grpcwebproxy 

* https://github.com/improbable-eng/grpc-web/blob/master/go/grpcwebproxy/README.md

，：

* `grpcwebproxy --backend_addr 127.0.0.1:9090 --run_tls_server=false --allow_all_origins`


