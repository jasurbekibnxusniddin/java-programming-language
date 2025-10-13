package service

import (
	"fmt"
	"go-server/genproto/voice_proto"
	"time"
)

type server struct {
	voice_proto.UnimplementedVoiceStreamServer
}

func NewServer() voice_proto.VoiceStreamServer {
	return &server{}
}

func (s *server) StreamText(req *voice_proto.VoiceRequest, stream voice_proto.VoiceStream_StreamTextServer) error {

	fmt.Printf("Client connected, session: %s\n", req.SessionId)

	texts := []string{
		"Hello there!",
		"This is your voice assistant.",
		"I'm transcribing your speech in real time.",
		"Almost done...",
		"Goodbye!",
	}

	for i, t := range texts {
		msg := &voice_proto.VoiceText{Text: t, Part: int32(i + 1)}
		if err := stream.Send(msg); err != nil {
			return err
		}
		time.Sleep(1 * time.Second)
	}

	fmt.Printf("Session %s completed.\n", req.SessionId)

	return nil
}
