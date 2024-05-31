package com.example.geminitelegrambot.controller;

import com.example.geminitelegrambot.dto.request.RootRequestDto;
import com.example.geminitelegrambot.dto.response.RootResponseDto;
import com.example.geminitelegrambot.service.GeminiService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Setter
@Getter
@RequiredArgsConstructor
@RequestMapping("/gemini")
public class GeminiController {
    private final GeminiService service;
    private Long lastUpdateId = 0L;

    @GetMapping
    public RootResponseDto msgResponse() {
        return service.sendResponse();
    }

    @Scheduled(fixedDelay = 1000)
    public synchronized void checkForMessages() {
        RootRequestDto updateService = service.getUpdateService();
        if (!updateService.getResult().isEmpty()) {
            Integer latestUpdateId = updateService.getResult().get(updateService.getResult().size() - 1).getUpdateId();
            if (latestUpdateId > lastUpdateId) {
                lastUpdateId = Long.valueOf(latestUpdateId);
                RootResponseDto response = service.sendResponse();
                if (response != null) {
                    lastUpdateId = 0L;
                }
            }
        }
    }
}
