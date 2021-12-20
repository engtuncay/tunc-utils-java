package ozpasyazilim.utils.gui.fxcomponents;

import javafx.scene.web.WebView;

public class FxWebViewHp {

	public static FxWebViewHp bui() {
		return new FxWebViewHp();
	}


	public static void showHtml(WebView webView, String txHtml) {
		webView.getEngine().loadContent(txHtml, "text/html");
	}
}
