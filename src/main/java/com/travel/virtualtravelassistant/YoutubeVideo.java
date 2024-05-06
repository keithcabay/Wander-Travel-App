package com.travel.virtualtravelassistant;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class YoutubeVideo  {

    @FXML
    private WebView webView;

    public WebView getWebView() {
        return webView;
    }
}
