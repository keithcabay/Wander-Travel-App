package com.travel.virtualtravelassistant.ChatGPT;

import com.travel.virtualtravelassistant.MainApplication;
import com.travel.virtualtravelassistant.User.CurrentUser;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.InputStream;
import java.util.Properties;

public class ChatbotUIController implements ChatbotAwareController {

    @FXML
    private VBox chatBox;
    @FXML
    private TextField inputField;
    private ChatGPTService chatGPTService;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    public void initialize() {
        loadProperties();
        setupChatBox();
        configureTextField();
        configureDynamicSizing();
    }

    private void loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("Cannot find application.properties");
            }
            properties.load(input);
            chatGPTService = ChatGPTService.getInstance(properties);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Failed to load properties", ex);
        }
    }

    private void setupChatBox() {
        chatBox.setPrefSize(400, 400);
        chatBox.setMinHeight(600); // Ensure it has a minimum height but can expand
    }

    private void configureTextField() {
        inputField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onSend(new ActionEvent());
            }
        });
    }

    @FXML
    private void onChatbotClick(ActionEvent event) {
        chatBox.setVisible(!chatBox.isVisible());
        if (chatBox.isVisible()) {
            inputField.requestFocus();
        }
    }

    @FXML
    private void onSend(ActionEvent event) {
        String userInput = inputField.getText().trim();
        if (!userInput.isEmpty()) {
            displayMessage(CurrentUser.getInstance().getUserInfo().getFirst_name() + ": " + userInput, true);
            inputField.clear();
            chatGPTService.generateResponse(userInput).thenAccept(response ->
                    Platform.runLater(() -> {
                        displayMessage("Wander: " + response, false);
                        scrollToBottom();
                    })
            );
        }
    }

    private void displayMessage(String message, boolean isUser) {
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(Double.MAX_VALUE); // Allow full text display

        messageLabel.getStyleClass().add(isUser ? "user-bubble" : "bot-bubble");

        HBox hbox = new HBox(messageLabel);
        hbox.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(5, 10, 5, 10));

        Platform.runLater(() -> {
            chatBox.getChildren().add(hbox);
            scrollPane.requestLayout();
            scrollPane.layout();
            scrollToBottom(); // Ensure the scroll pane scrolls to show the latest message
        });
    }

    private void scrollToBottom() {
        Platform.runLater(() -> {
            scrollPane.setVvalue(1.0); // Scroll to the very bottom
        });
    }

    private void configureDynamicSizing() {
        scrollPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            double newWidth = newValue.doubleValue() * 0.75; // Adjust the factor as needed
            for (Node node : chatBox.getChildren()) {
                if (node instanceof HBox) {
                    HBox hbox = (HBox) node;
                    for (Node innerNode : hbox.getChildren()) {
                        if (innerNode instanceof Label) {
                            ((Label) innerNode).setMaxWidth(newWidth);
                        }
                    }
                }
            }
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
