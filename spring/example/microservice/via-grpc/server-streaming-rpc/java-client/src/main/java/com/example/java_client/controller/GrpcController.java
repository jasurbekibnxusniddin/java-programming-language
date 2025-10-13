package com.example.java_client.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.java_client.service.GrpcVoiceClient;

@RestController
public class GrpcController {

    private final GrpcVoiceClient grpcVoiceClient;

    public GrpcController(GrpcVoiceClient grpcVoiceClient) {
        this.grpcVoiceClient = grpcVoiceClient;
    }

    @GetMapping("/grpc/stream")
    public String stream() throws InterruptedException {
        grpcVoiceClient.streamTexts();
        return "Streaming started!";
    }

    @GetMapping(value = "/grpc/stream/live", produces = "text/event-stream")
    public SseEmitter streamLive() {
        SseEmitter emitter = new SseEmitter(0L); // 0L = no timeout
        new Thread(() -> grpcVoiceClient.streamTextsLive(emitter)).start();
        return emitter;
    }
}
