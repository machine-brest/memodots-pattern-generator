package sample;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import sample.view.DotPattern;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class App extends Application
{
	private class AppController implements Initializable
	{
		private Pane root;

		/**
		 * UI control binding
		 */
		@FXML private Pane contentPane;
		@FXML private Pane commandPane;
		@FXML private Button generateButton;

		private DotPattern pattern;

		public void start(Stage primaryStage)
		{
			try {
				setUserAgentStylesheet(STYLESHEET_MODENA);

				FXMLLoader loader = new FXMLLoader();

				loader.setLocation(AppController.class.getResource("view/app-shell.fxml"));
				loader.setController(this);
				root = loader.load();
			}
			catch (IOException ex) {
				ex.printStackTrace();
				System.exit(1);
			}

			initContentPane(contentPane);

			primaryStage.setScene(new Scene(root));
			primaryStage.setResizable(true);
			primaryStage.setWidth(400);
			primaryStage.setHeight(550);
			primaryStage.setTitle("MemoDOTS pattern generator");
			primaryStage.show();
		}

		@FXML Group gridBackground;
		@FXML Group levelPattern;
		@FXML Group userPattern;

		protected void initContentPane(Pane parent) {

			// parent.setPrefSize(500, 800);
			// parent.autosize();

			byte gridXCount = 8;
			byte gridYCount = 8;

			for (byte col = 0; col < gridXCount; ++col)
				for (byte row = 0; row < gridYCount; ++row) {
					Circle dot = new Circle(col * 40.0, row * 40, 8.0);
					gridBackground.getChildren().add(dot);
				}
		}

		@FXML
		public void actionGenerate() {

			levelPattern.setVisible(false);
			levelPattern.getChildren().clear();

			pattern = new DotPattern(8, 8);

			pattern.setDotsPerPattern(26);
			pattern.setShapesPerPattern(3);
			pattern.setAllowDiagonals(false);
			pattern.setAllowOpen(false);
			pattern.generate();

			// draw shapes

			/*
			for (byte col = 0; col < gridXCount; ++col)
				for (byte row = 0; row < gridYCount; ++row) {
					Circle dot = new Circle(col * 40.0, row * 40, 5.0);
					levelPattern.getChildren().add(dot);
				}
			*/

			levelPattern.setVisible(true);
		}

		@Override
		public void initialize(URL location, ResourceBundle resources) {

			/*
			generateButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {

				}
			});
			*/
		}
	}

	@Override
	public void start(Stage primaryStage) {
		new AppController().start(primaryStage);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
