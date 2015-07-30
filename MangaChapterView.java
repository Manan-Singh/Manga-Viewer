import java.io.IOException;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MangaChapterView {

	private String url;
	private Stage stage;
	private Scene scene;
	private BorderPane root;
	private ImageView viewer;
	private ArrayList<Image> chapter;
	private ArrayList<String> pageLocations;
	private Document firstPage;
	private int pageNumber = 0;
	
	public MangaChapterView(String url) throws IOException{
		this.url = url;
		firstPage = Jsoup.connect(url).get();
		stage = new Stage();
		root = new BorderPane();
		root.setStyle("-fx-background-color: black;");
		scene = new Scene(root);
		viewer = new ImageView();
		root.setCenter(viewer);
		stage.setScene(scene);
		stage.show();
		extractImages();
		viewer.setFitHeight(700);
		viewer.setFitWidth(700);
		scene.setOnKeyPressed(new EventHandler<KeyEvent>(){

			public void handle(KeyEvent e) {
				if(e.getCode() == KeyCode.RIGHT && pageNumber < pageLocations.size()){
					loadImage(pageLocations.get(++pageNumber));
				}
			}});
		
		loadImage(pageLocations.get(pageNumber));
	}
	
	private void extractImages() throws IOException{
		Elements linksToPages = firstPage.getElementById("pageMenu").getElementsByTag("option");
		chapter = new ArrayList<Image>();
		pageLocations = new ArrayList<String>();
		for(Element x : linksToPages){
			Document page = Jsoup.connect(PandaRetriever.mainURL + x.attr("value")).get();
			pageLocations.add(page.getElementById("img").attr("src"));
		}
	}
	
	private void loadImage(String url){
		Image image = new Image(url);
		viewer.setImage(image);
	}
}
