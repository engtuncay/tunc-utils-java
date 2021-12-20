package ozpasyazilim.utils.core;

import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class FiHtmlRender {

	public static void toBase64(){
		Path path = Paths.get("...");
		InputStream in = null;
		try {
			in = Base64.getDecoder().wrap(Files.newInputStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Image image = new Image(in);

	}

	public static void toBase64V2(){

		Path path = Paths.get("...");

		try {
			byte[] bytes = Files.readAllBytes(path);
			byte[] img = Base64.getDecoder().decode(bytes);


		} catch (IOException e) {
			e.printStackTrace();
		}

	}


}
