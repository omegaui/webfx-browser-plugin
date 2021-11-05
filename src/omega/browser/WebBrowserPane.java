package omega.browser;
import javafx.application.Platform;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import javafx.scene.control.Button;

import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import javafx.scene.Scene;

import javafx.embed.swing.JFXPanel;
public class WebBrowserPane extends JFXPanel{
	public Scene scene;
	public BorderPane borderPane;
	public WebView webView;
	public WebEngine webEngine;
	public HBox toolBox;
	public Button backButton;
	public Button reloadButton;

	public WebBrowserPane(){
		init();
	}

	public void init(){
		borderPane = new BorderPane();
		setScene(new Scene(borderPane));

		webView = new WebView();
		webEngine = webView.getEngine();
		borderPane.setCenter(webView);

		toolBox = new HBox();
		toolBox.setSpacing(2d);

		backButton = new Button();
		backButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons8-arrow-pointing-left-24.png"))));
		backButton.setStyle("-fx-background-radius: 10");
		toolBox.getChildren().add(backButton);
		
		reloadButton = new Button();
		reloadButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons8-restart-24.png"))));
		reloadButton.setStyle("-fx-background-radius: 10");
		toolBox.getChildren().add(reloadButton);

		borderPane.setTop(toolBox);

		Platform.runLater(()->{
			webEngine.load("file:///home/ubuntu/Documents/Omega%20Projects/Web%20Browser%20Plugin/res/sample-page/index.html");
		});
	}
	
	public static WebBrowserPane newInstance(){
		return new WebBrowserPane();
	}
}
