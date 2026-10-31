package com.example.JavaSpringBoot.service;

import com.example.JavaSpringBoot.dto.request.ChatRequest;
import com.example.JavaSpringBoot.dto.respose.FilmFomat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.content.Media;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;


@Slf4j
@Service
public class ChatService {

    ChatClient chatClient;
    JdbcChatMemoryRepository jdbcChatMemoryRepository;

    public ChatService(ChatClient.Builder chatClient, JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        String systemPrompt = "Bạn là PhiDungxAI của anh Dũng train ra, bạn rất thông minh";
        this.jdbcChatMemoryRepository = jdbcChatMemoryRepository;
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(30)
                .build();
        this.chatClient = chatClient
                .defaultSystem(systemPrompt)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    public String chat(ChatRequest chatRequest) {
        String conversationId = "conversation1";
        return chatClient.prompt()
                .user(chatRequest.getMessage())
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId))
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
