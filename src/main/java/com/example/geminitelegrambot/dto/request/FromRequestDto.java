package com.example.geminitelegrambot.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FromRequestDto {
    private Long id;
    @JsonProperty("is_bot")
    private Boolean isBot;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("language_code")
    private String languageCode;
}

