package com.example.JavaSpringBoot.service;

import com.example.JavaSpringBoot.dto.request.ChatRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    ChatClient chatClient;
    public ChatService(ChatClient.Builder chatClient) {
        String systemPrompt = "Bạn là PhiDungxAI của anh Dũng train ra, bạn rất thông minh";
        this.chatClient = chatClient.defaultSystem(systemPrompt).build();
    }

    public String chat(ChatRequest chatRequest) {
        return chatClient.prompt()
                .user(chatRequest.getMessage())
                .call()
                .content();
    }

}
