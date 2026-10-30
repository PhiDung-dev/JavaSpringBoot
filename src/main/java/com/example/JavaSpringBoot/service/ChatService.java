package com.example.JavaSpringBoot.service;

import com.example.JavaSpringBoot.dto.request.ChatRequest;
import com.example.JavaSpringBoot.dto.respose.FilmFomat;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.content.Media;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;


@Service
public class ChatService {

    ChatClient chatClient;
    public ChatService(ChatClient.Builder chatClient) {
        String systemPrompt = "Bạn là PhiDungxAI của anh Dũng train ra, bạn rất thông minh";
        this.chatClient = chatClient.defaultSystem(systemPrompt).build();
    }

    public List<FilmFomat> chat(ChatRequest chatRequest) {
        return chatClient.prompt()
                .user(chatRequest.getMessage())
                .call()
                .entity(new ParameterizedTypeReference<List<FilmFomat>>() {
                });
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
