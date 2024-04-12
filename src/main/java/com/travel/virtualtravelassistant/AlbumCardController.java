package com.travel.virtualtravelassistant;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import java.util.Objects;

public class AlbumCardController {
    @FXML
    ImageView albumPreviewImage;

    @FXML
    Label title;

    public void setAlbumInfoWithImage(Album album){
        Image image = (album.getCurrImage());
        albumPreviewImage.setImage(image);
        albumPreviewImage.setFitHeight(450);
        System.out.println("Set Album info: " + album.getTitle());
        title.setText(album.getTitle());
    }

    public void setAlbumInfo(Album album){
        System.out.println("Set Album Info: " + album.getAlbumCover());
        Image image = new Image("file:" + album.getAlbumCover());
        albumPreviewImage.setImage(image);
        albumPreviewImage.setFitHeight(450);
        System.out.println("Set Album info: " + album.getTitle());
        title.setText(album.getTitle());
    }
}
