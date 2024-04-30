package com.travel.virtualtravelassistant.ChatGPT;

import com.travel.virtualtravelassistant.MainApplication;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.InputStream;
import java.util.Properties;

public class ChatbotUIController implements ChatbotAwareController {

    private static final double WIDTH = 200;
    private static final double HEIGHT = 400;

    @FXML
    private VBox chatBox;
    @FXML
    private TextField inputField;
    private ChatGPTService chatGPTService;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    public void initialize() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("Cannot find application.properties");
            }
            properties.load(input);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Failed to load properties", ex);
        }

        // Correctly get instance of ChatGPTService with properties loaded
        chatGPTService = ChatGPTService.getInstance(properties);

        chatBox.setPrefSize(WIDTH, HEIGHT);
        chatBox.setMinSize(WIDTH, HEIGHT);
        chatBox.setMaxSize(WIDTH, HEIGHT);
    }

    @FXML
    private void onChatbotClick(ActionEvent event) {
        chatBox.setVisible(!chatBox.isVisible()); // Toggle visibility
        if (chatBox.isVisible()) {
            inputField.requestFocus();
        }
    }

    @FXML
    private void onSend(ActionEvent event) {
        String userInput = inputField.getText().trim();
        if (!userInput.isEmpty()) {
            displayMessage("User: " + userInput, true);
            inputField.clear();
            chatGPTService.generateResponse(userInput).thenAccept(response ->
                    Platform.runLater(() -> displayMessage("Wander: " + response, false))
            );
        }
    }

    private void displayMessage(String message, boolean isUser) {
        Label messageLabel = new Label(message);
        messageLabel.getStyleClass().add(isUser ? "user-bubble" : "bot-bubble");
        messageLabel.setWrapText(true);

        HBox hbox = new HBox(messageLabel);
        hbox.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        hbox.setMaxWidth(chatBox.getWidth() - 20);
        hbox.setPadding(new Insets(5, 10, 5, 10));

        Platform.runLater(() -> {
            chatBox.getChildren().add(hbox);
            scrollPane.setVvalue(1.0); // This line ensures the scrollPane scrolls to the bottom
        });
    }

    @Override
    public void setChatGPTService(ChatGPTService service) {
        this.chatGPTService = service;
    }

    @FXML
    private void handleClose(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide(); // or .close();
    }

    public void onHide(){
        MainApplication.showOrHideChatbot();
    }

}
