package ruo.cmplus.cm.v18;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.Display;

import java.util.Timer;
import java.util.TimerTask;

public class HTMLFX extends Application {
	private String url;
	private Minecraft mc = Minecraft.getMinecraft();
	private ScaledResolution sr = new ScaledResolution(mc);
	public HTMLFX(String url) {
		this.url = url;
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		if(url.indexOf("youtu") != -1){
			url = url.replace("watch?v=", "embed/").replace("https", "http").replace("&feature=youtu.be", "") + "?autoplay=1&autohide=1&controls=0&showinfo=0&rel=0";
			WebView webview = new WebView();
			webview.getEngine().load(url);
			webview.setPrefSize(sr.getScaledWidth(), sr.getScaledHeight());
			stage.setScene(new Scene(webview));
			ad(stage, webview);

		}else if(url.startsWith("http")){
			WebView webview = new WebView();
			webview.getEngine().load(url);
			webview.setPrefSize(sr.getScaledWidth(), sr.getScaledHeight());
			stage.setScene(new Scene(webview));
		   ad(stage, webview);

		}else{
			stage.setTitle("Media");
		    Group root = new Group();
		    Media media = new Media(url);
		    MediaPlayer mediaPlayer = new MediaPlayer(media);
		    mediaPlayer.play();
		    MediaView mediaView = new MediaView(mediaPlayer);
		    mediaView.setFitWidth(sr.getScaledWidth());
		    mediaView.setFitHeight(sr.getScaledHeight());
		    mediaView.setX(Display.getX());
		    mediaView.setY(Display.getY());
		    mediaView.setTranslateZ(100);
		    root.getChildren().add(mediaView);
		    stage.setScene(new Scene(root));
		}
		
	}

	public void ad(final Stage stage, final WebView webview){
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						System.out.println(webview.getEngine().getLoadWorker().getProgress());
						if(webview.getEngine().getLoadWorker().getProgress() == 1.0){
							stage.show();
							timer.cancel();
						}
					}
				});
			}
		}, 100, 100);
	}
}
