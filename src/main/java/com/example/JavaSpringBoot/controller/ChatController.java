package com.example.JavaSpringBoot.controller;

import com.example.JavaSpringBoot.dto.request.ChatRequest;
import com.example.JavaSpringBoot.dto.respose.FilmFomat;
import com.example.JavaSpringBoot.service.ChatService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/chatbot")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatController {

    ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public List<FilmFomat> chat(@RequestBody ChatRequest request) {
        return chatService.chat(request);
    }

    @PostMapping("/chatWithImage")
    public String chatWithImage(@RequestParam("file")MultipartFile file, @RequestParam("message") String message) {
        return chatService.chatWithImage(file, message);
    }

}
