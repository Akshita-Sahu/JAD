package com.akshita.jad.grpc.server;

/**
 * @author: FengYe
 * @date: 2024/10/13 02:40
 * @description: JADGrpcServerBootstrap
 */
public class JADGrpcBootstrap {
    public static void main(String[] args) {
        JADGrpcServer jadGrpcServer = new JADGrpcServer(9091, null);
        jadGrpcServer.start();
    }
}
