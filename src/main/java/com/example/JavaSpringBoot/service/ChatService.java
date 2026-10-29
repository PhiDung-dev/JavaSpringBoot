package com.example.JavaSpringBoot.service;

import com.example.JavaSpringBoot.dto.request.ChatRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.content.Media;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;


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

    public String chatWithImage(MultipartFile file, String message) {
        Media media = Media.builder()
                .mimeType(MimeTypeUtils.parseMimeType(file.getContentType()))
                .data(file.getResource())
                .build();
        return chatClient.prompt()
                .user(promptUserSpec -> promptUserSpec.media(media).text(message))
                .call()
                .content();
    }
}
