import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PandaRetriever {
	
	public static String mainURL;
	private Document homePage;
	private Element popularMangaSection;
	
	public PandaRetriever() throws IOException{
		mainURL = "http://mangapanda.com";
		homePage = Jsoup.connect(mainURL).get();
		popularMangaSection = homePage.getElementById("popularlist");
	}
	
	public ObservableList<String> getPopMangaNames(){
		ObservableList<String> names = FXCollections.observableArrayList();
		for(Element x : popularMangaSection.getElementsByClass("popularitemcaption")){
			names.add(new String(x.ownText()));
		}
		return names;
	}
	
	public void generateChaptersPage(String mangaName){
		Element chosenElement = popularMangaSection.select("a:contains(" + mangaName + ")").get(0);
		//System.out.println(mainURL + chosenElement.attr("href"));
		Document chapterPage = null;
		ObservableList<String> chapts = FXCollections.observableArrayList();
		try {
			//Connect to the URL of the page with the chosen manga's chapters
			chapterPage = Jsoup.connect(mainURL + chosenElement.attr("href")).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Elements chapterElements = chapterPage.select("tr td:has(div.chico_manga)");
		for(Element x : chapterElements){
			chapts.add(x.text());
		}
		Stage stage = new Stage();
		stage.setTitle(mangaName);
		BorderPane tempRoot = new BorderPane();
		Scene scene = new Scene(tempRoot, 500, 500);
		stage.setScene(scene);
		stage.show();
		ListView lv = new ListView(chapts);
		lv.setOnMouseClicked(new EventHandler<MouseEvent>(){

			public void handle(MouseEvent e) {
				try {
					stage.close();
					new MangaChapterView(mainURL + chapterElements.get(lv.getSelectionModel().getSelectedIndex()).getElementsByTag("a").attr("href"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}});
		tempRoot.setCenter(lv);
	}
}
