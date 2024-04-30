package com.travel.virtualtravelassistant.ChatGPT.AIModel;

import com.travel.virtualtravelassistant.ChatGPT.AssistantAIClient;
import com.travel.virtualtravelassistant.ChatGPT.dto.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static java.lang.Thread.sleep;


public class GPTMethods {

    private StringBuilder stringBuilder = new StringBuilder();
    private int messageProcessCount = 0;

    int counter;

    public GPTMethods() {
        this.stringBuilder = new StringBuilder();
    }


    public int counter() {
        counter++;
        return counter;
    }

    private void waitUntilRunIsFinished(AssistantAIClient client, ThreadResponseDTO thread, RunResponseDTO run, long DELAY) throws InterruptedException {
        while (!isRunDone(client, thread.id(), run.id())) {
            superviseWorkInProgress(client, thread);
            sleep(DELAY * 1000);
        }
    }

    private void superviseWorkInProgress(AssistantAIClient client, ThreadResponseDTO thread) {
        try {
            System.out.println("Checking"); //messages to supervise assistant's work");
            MessagesListResponseDTO messages = client.getMessages(thread.id());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void log(MessagesListResponseDTO messages, TextArea outputTF, TextField inputTF) {
        messageProcessCount = 0;
        for (MessageResponseDTO message : messages.data()) {
            if (messageProcessCount < 1) {
                for (MessageResponseDTO.Content content : message.content()) {
                    String textContent = content.text().value();
                    System.out.println(textContent);

                    String newResponse = organizeStringByWordCount(textContent, 20);
                    outputTF.setText(newResponse);

                    stringBuilder.append("ChatGPT:").append(newResponse).append("\n");
                    messageProcessCount++;
                    inputTF.clear();
                    break;
                }
            }
        }
    }

    private void log2(MessagesListResponseDTO messages, TextArea outputTF, TextField inputTF) {
        messageProcessCount = 0;
        for (MessageResponseDTO message : messages.data()) {
            if (messageProcessCount < 1) {
                for (MessageResponseDTO.Content content : message.content()) {
                    String textContent = content.text().value();
                    System.out.println(textContent);

                    String newResponse = organizeStringByWordCount(textContent, 20);
                    outputTF.setText(newResponse);

                    //stringBuilder.append("ChatGPT:").append(newResponse).append("\n");
                    messageProcessCount++;
                    inputTF.clear();
                    break;
                }
            }
        }
    }

    private static boolean isRunDone(AssistantAIClient client, String threadId, String runId) {
        RunResponseDTO status;
        try {
            status = client.getRunStatus(threadId, runId);
            System.out.println("Status of your run is currently " + status);
            return isRunStateFinal(status);
        } catch (Exception e) {
            System.err.println("Failed to get run state, will retry..." + e);
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isRunStateFinal(RunResponseDTO runResponseDTO) {
        List<String> finalStates = List.of("cancelled", "failed", "completed", "expired"); //I consider these states as final
        String runStatus = Optional.of(runResponseDTO).map(RunResponseDTO::status).orElse("unknown").toLowerCase();
        return finalStates.contains(runStatus);
    }

    public void initializeAssistant(TextArea outputTF, TextField inputTF, String initialPrompt) {
        stringBuilder.append("User: ");
        stringBuilder.append(inputTF.getText());
        stringBuilder.append("\n");
        Properties properties = new Properties();
        long DELAY = 3;

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find application.properties");
                return;
            }
            properties.load(input);

            AssistantAIClient client = new AssistantAIClient(properties);
            AssistantResponseDTO assistant = client.createAssistant(initialPrompt);
            ThreadResponseDTO thread = client.createThread();
            String request = String.valueOf(stringBuilder);
            System.out.println(request);
            client.sendMessage(thread.id(), "user", request);
            RunResponseDTO run = client.runMessage(thread.id(), assistant.id());

            waitUntilRunIsFinished(client, thread, run, DELAY);

            MessagesListResponseDTO allResponses = client.getMessages(thread.id());
            log(allResponses, outputTF, inputTF);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void initializeAssistant2(TextArea outputTF, TextField inputTF, String initialPrompt) {
//        stringBuilder.append("User: ");

        stringBuilder.append(inputTF.getText());
//        stringBuilder.append("\n");
        Properties properties = new Properties();
        long DELAY = 3;

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find application.properties");
                return;
            }
            properties.load(input);

            AssistantAIClient client = new AssistantAIClient(properties);
            AssistantResponseDTO assistant = client.createAssistant(initialPrompt);
            ThreadResponseDTO thread = client.createThread();
            String request = String.valueOf(inputTF.getText());
            System.out.println(request);
            client.sendMessage(thread.id(), "user", request);
            RunResponseDTO run = client.runMessage(thread.id(), assistant.id());

            waitUntilRunIsFinished(client, thread, run, DELAY);

            MessagesListResponseDTO allResponses = client.getMessages(thread.id());
            log2(allResponses, outputTF, inputTF);
            System.out.println(inputTF.getText());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String organizeStringByWordCount(String input, int wordCount) {
        String[] words = input.split(" ");
        StringBuilder result = new StringBuilder();
        int count = 0;

        for (String word : words) {
            result.append(word).append(" ");
            count++;


            if (count % wordCount == 0) {
                result.append("\n");
            }
        }

        return result.toString().trim();
    }


}
