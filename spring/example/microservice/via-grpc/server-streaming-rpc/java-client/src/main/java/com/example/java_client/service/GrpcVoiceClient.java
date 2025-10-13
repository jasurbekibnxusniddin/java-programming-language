package com.example.java_client.service;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.java_client.proto.VoiceRequest;
import com.example.java_client.proto.VoiceStreamGrpc;
import com.example.java_client.proto.VoiceText;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

@Service
public class GrpcVoiceClient {

    // existing method for local testing
    public void streamTexts() throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        VoiceStreamGrpc.VoiceStreamStub stub = VoiceStreamGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);

        VoiceRequest request = VoiceRequest.newBuilder()
                .setSessionId("abc123")
                .build();

        stub.streamText(request, new StreamObserver<VoiceText>() {
            @Override
            public void onNext(VoiceText value) {
                System.out.printf("Received text[%d]: %s%n", value.getPart(), value.getText());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Stream error: " + t.getMessage());
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("Stream completed.");
                latch.countDown();
            }
        });

        latch.await(5, TimeUnit.MINUTES);
        channel.shutdownNow();
    }

    // ✅ NEW METHOD: stream gRPC responses to frontend in real time via SSE
    public void streamTextsLive(SseEmitter emitter) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        VoiceStreamGrpc.VoiceStreamStub stub = VoiceStreamGrpc.newStub(channel);

        VoiceRequest request = VoiceRequest.newBuilder()
                .setSessionId("live-session-001")
                .build();

        stub.streamText(request, new StreamObserver<VoiceText>() {
            @Override
            public void onNext(VoiceText value) {
                try {
                    System.out.printf("Streaming text[%d]: %s%n", value.getPart(), value.getText());
                    emitter.send(SseEmitter.event()
                            .name("voice-text")
                            .data(value.getText()));
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Stream error: " + t.getMessage());
                emitter.completeWithError(t);
                channel.shutdownNow();
            }

            @Override
            public void onCompleted() {
                System.out.println("Stream completed.");
                emitter.complete();
                channel.shutdownNow();
            }
        });
    }
}
