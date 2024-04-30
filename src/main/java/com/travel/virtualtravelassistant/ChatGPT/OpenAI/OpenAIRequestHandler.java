package com.travel.virtualtravelassistant.ChatGPT.OpenAI;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;

public class OpenAIRequestHandler {
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = System.getenv("OpenAI API");
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    public static Gson getGson() {
        return gson;
    }
}

