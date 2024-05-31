package com.example.geminitelegrambot.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RootResponseDto {
    private Boolean ok;
    private ResultResponseDto result;
}
