package main

import (
	"go-server/genproto/voice_proto"
	"go-server/src/service"
	"log"
	"net"

	"google.golang.org/grpc"
	"google.golang.org/grpc/reflection"
)

func main() {
	
	lis, err := net.Listen("tcp", ":9090")
	if err != nil {
		log.Fatalf("Failed to listen: %v", err)
	}

	s := grpc.NewServer()
	voice_proto.RegisterVoiceStreamServer(s, service.NewServer())
	reflection.Register(s)

	log.Println("Voice streaming server running on :9090")
	if err := s.Serve(lis); err != nil {
		log.Fatal(err)
	}
}
