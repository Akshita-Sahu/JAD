package com.akshita.jad.grpcweb.proxy.server;

import com.akshita.jad.grpcweb.proxy.server.grpcService.EchoImpl;
import com.akshita.jad.grpcweb.proxy.server.grpcService.GreeterService;
import com.akshita.jad.grpcweb.proxy.server.grpcService.HelloImpl;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class StartGrpcTest {

    private int GRPC_PORT;

    public StartGrpcTest(int grpcPort){
        this.GRPC_PORT = grpcPort;
    }

    public void startGrpcService(){
        try {
            Server grpcServer = ServerBuilder.forPort(GRPC_PORT).addService((BindableService) new GreeterService())
                    .addService((BindableService) new HelloImpl()).addService(new EchoImpl()).build();
            grpcServer.start();
            System.out.println("started gRPC server on port # " + GRPC_PORT);
            System.in.read();
        } catch (IOException e) {
            System.out.println("fail to start gRPC server");
            throw new RuntimeException(e);
        }
    }
}
