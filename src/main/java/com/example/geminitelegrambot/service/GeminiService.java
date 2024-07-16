package com.example.geminitelegrambot.service;

import com.example.geminitelegrambot.client.GeminiApiClient;
import com.example.geminitelegrambot.client.TelegramApiClient;
import com.example.geminitelegrambot.dto.gemini.Candidate;
import com.example.geminitelegrambot.dto.gemini.Root;
import com.example.geminitelegrambot.dto.request.RootRequestDto;
import com.example.geminitelegrambot.dto.request.TelegramSendDto;
import com.example.geminitelegrambot.dto.response.RootResponseDto;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Setter
@Getter
@Slf4j
public class GeminiService {
    private final GeminiApiClient geminiApiClient;
    private final TelegramApiClient telegramApiClient;
    private final String key = "YOUR_API_KEY";
    private Long lastUpdateId = 0L;

    public GeminiService(GeminiApiClient geminiApiClient,
                         TelegramApiClient telegramApiClient) {
        this.geminiApiClient = geminiApiClient;
        this.telegramApiClient = telegramApiClient;
    }

    public RootRequestDto getUpdateService() {
        RootRequestDto updates = telegramApiClient.getUpdates(0L);
        Integer updateId = updates.getResult().get(updates.getResult().size() - 1).getUpdateId();
        log.info("Message got from - " + updates.getResult().get(0).getMessage().getFrom().getFirstName() +
                ", ID - " + updates.getResult().get(0).getMessage().getChat().getId());
        return telegramApiClient.getUpdates(Long.valueOf(updateId));
    }

    private String extractTextFromGeminiResponse(Root updates) {
        StringBuilder textBuilder = new StringBuilder();

        if (updates.getCandidates() != null) {
            for (Candidate candidate : updates.getCandidates()) {
                String text = candidate.getContent().getParts().get(0).getText();
                text = text.replace("*", "");
                textBuilder.append(text).append("\n\n");
            }
        }

        return textBuilder.toString().trim();
    }

    public Root getUpdates(String messageText) {
        try {
            JsonObject json = new JsonObject();
            JsonArray contentsArray = new JsonArray();
            JsonObject contentsObject = new JsonObject();
            JsonArray partsArray = new JsonArray();
            JsonObject partsObject = new JsonObject();

            partsObject.add("text", new JsonPrimitive(messageText));
            partsArray.add(partsObject);
            contentsObject.add("parts", partsArray);
            contentsArray.add(contentsObject);
            json.add("contents", contentsArray);

            String content = json.toString();
            return geminiApiClient.getData(key, content);
        } catch (Exception e) {
            log.error("Error while getting updates from Gemini API: ", e);
            throw e;
        }
    }

    public RootResponseDto sendMessage(TelegramSendDto dto) {
        return telegramApiClient.sendMessage(dto);
    }

    public RootResponseDto sendResponse() {
        RootRequestDto updateService = getUpdateService();
        String text = updateService.getResult().get(0).getMessage().getText();
        Long id = updateService.getResult().get(0).getMessage().getChat().getId();
        TelegramSendDto dto = new TelegramSendDto();
        dto.setChatId(String.valueOf(id));

        if (text.equals("/start")) {
            String msg = "Hi " + updateService.getResult().get(0).getMessage().getFrom().getFirstName() +
                    ", welcome to Gemini 1.5 Flash bot!\n\nHow can I help you?";

            dto.setText(msg);
            sendMessage(dto);
            log.info("Message sent to - " + updateService.getResult().get(0).getMessage().getFrom().getFirstName());
        } else {
            Root updates = getUpdates(text);
            String extractedText = extractTextFromGeminiResponse(updates);
            dto.setText(extractedText);
            sendMessage(dto);
        }

        return null;
    }
}
