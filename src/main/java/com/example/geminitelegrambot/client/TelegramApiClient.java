package com.example.geminitelegrambot.client;


import com.example.geminitelegrambot.dto.request.RootRequestDto;
import com.example.geminitelegrambot.dto.request.TelegramSendDto;
import com.example.geminitelegrambot.dto.response.RootResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "telegramApi", url = "https://api.telegram.org/bot7157061251:AAHAZG-vKj_J111pcel-d3vEZXFDXFte7xw")
public interface TelegramApiClient {
    @GetMapping("/getUpdates?offset={value}")
    RootRequestDto getUpdates(@PathVariable Long value);

    @PostMapping("/sendMessage")
    RootResponseDto sendMessage(@RequestBody TelegramSendDto dto);
}
