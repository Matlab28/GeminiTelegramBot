package com.example.geminitelegrambot.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatRequestDto {
    private Long id;
    @JsonProperty("first_name")
    private String firstName;
    private String type;
}
