package com.travel.virtualtravelassistant.Utility;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class ApplicationUtil {
    public static void setImageViewRoundCroppedProfilePic(ImageView imageView, Image profile_pic){
//        Circle clip = new Circle();
//        clip.setRadius(50); // Adjust as needed
//
//        imageView.setClip(clip);
//        imageView.setImage(profile_pic);
//
//        double scaleFactor = Math.max(100 / profile_pic.getWidth(), 100 / profile_pic.getHeight());
//        imageView.setFitWidth(profile_pic.getWidth() * scaleFactor);
//        imageView.setFitHeight(profile_pic.getHeight() * scaleFactor);
//        clip.setCenterX(profile_pic.getWidth() * scaleFactor); // Adjust as needed
//        clip.setCenterY(profile_pic.getHeight() * scaleFactor); // Adjust as needed
//        imageView.setPreserveRatio(true);
        imageView.setImage(profile_pic);
    }
}
