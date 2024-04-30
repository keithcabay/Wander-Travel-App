package com.travel.virtualtravelassistant.ChatGPT.OpenAI;

import java.util.List;

public class RequestBodyData {
    List<Message> messages;
    String model;

    public RequestBodyData(List<Message> messages, String model) {
        this.messages = messages;
        this.model = model;
    }

    public static class Message {
        String role;
        String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}