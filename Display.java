import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.jsoup.nodes.Element;

public class Display extends Application{
	
	private Stage primaryStage;
	private Scene mainScene;
	private BorderPane root;
	//Menu bar for the entire app
	private MenuBar menuBar;
	private final Menu popMang = new Menu("Popular Manga");
	//Handles retrieving all of the information from the website
	private PandaRetriever pr;
	//Displaying popular manga names
	private ListView nameList;
	
	public static void main(String[] args){
		launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception{
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Mango - The Manga Viewer");
		root = new BorderPane();
		menuBar = new MenuBar();
		root.setTop(menuBar);
		menuBar.getMenus().add(popMang);
		mainScene = new Scene(root, 600, 600);
		primaryStage.setScene(mainScene);
		primaryStage.show();
		loadWebData();
		root.setCenter(nameList);
	}
	
	//Uses the PandaRetriever to get all of the information necessary that needs to be displayed to
	//the listviews
	public void loadWebData(){
		try{
			pr = new PandaRetriever();
		}catch(IOException e){
			e.printStackTrace();
		}
		nameList = new ListView(pr.getPopMangaNames());
		nameList.setOnMouseClicked(new EventHandler<MouseEvent>(){

			public void handle(MouseEvent e) {
				pr.generateChaptersPage((String) nameList.getSelectionModel().getSelectedItem());
			}
			
		});
	}
}
