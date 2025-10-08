package com.example.api_gateway.grpc;

import java.io.IOException;

import org.springframework.stereotype.Component;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class GatewayGrpcServer {

    private Server server;

    @PostConstruct
    public void start() throws IOException {
        server = ServerBuilder.forPort(9090) // gRPC server port
                .addService(new GatewayServiceImpl())
                .build()
                .start();
        System.out.println("✅ gRPC Server started on port 9090");
    }

    @PreDestroy
    public void stop() {
        if (server != null) {
            server.shutdown();
            System.out.println("🛑 gRPC Server stopped");
        }
    }

    static class GatewayServiceImpl extends GatewayServiceGrpc.GatewayServiceImplBase {

        // @Override
        // public void sendRequest(GatewayProto.GatewayRequest request,
        //         StreamObserver<GatewayProto.GatewayResponse> responseObserver) {
        //     System.out.println("📩 Received request: " + request.getMessage());

        //     GatewayProto.GatewayResponse response = GatewayProto.GatewayResponse.newBuilder()
        //             .setMessage("Hello from API Gateway! You said: " + request.getMessage())
        //             .build();

        //     responseObserver.onNext(response);
        //     responseObserver.onCompleted();
        // }
    }
}
