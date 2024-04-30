package com.travel.virtualtravelassistant.ChatGPT.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RunRequestDTO (
        @JsonProperty("assistant_id")
        String assistantId) {}