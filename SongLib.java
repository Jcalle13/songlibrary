package com.perezcalle.songlibrary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;


public class SongLib extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("MainScene.fxml"));
			AnchorPane rootLayout = loader.load();
			MainSceneController controller = loader.getController();
			controller.start(primaryStage);
			Scene scene = new Scene(rootLayout, 936, 674);

			primaryStage.setTitle("Song library");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
			stop();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
