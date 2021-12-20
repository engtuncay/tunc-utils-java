package ozpasyazilim.utils.gui.fxcomponents;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.icons525.Icons525;
import de.jensd.fx.glyphs.icons525.Icons525View;
import javafx.scene.control.Button;

public class FxAwesomeIcon {

	public Icons525View genIcon525(Icons525 icon525, Integer length, String fillColor, String strokeColor) {

		Icons525View iconSample = new Icons525View(icon525); //Icons525.EXCEL
		//iconSample.setGlyphStyle(String.format("-glyph-name:'%s';-fx-fill: %s;-fx-stroke:%s;-glyph-size: %dpx; ", name, fillColor, strokeColor, length));

		String format = "";

		if(length!=null)  format = String.format( format + "-glyph-size: %dpx;", length);

		if(fillColor!=null)format = String.format( format + " -fx-fill: %s;", fillColor);

		if(strokeColor!=null) format = String.format( format + " -fx-stroke: %s;", strokeColor);

		if(!format.equals("")) iconSample.setGlyphStyle(format);

		//iconSample.setStyle("-fx-fill: blue");
		//iconSample.setStyle("-icons-color: yellow");

		return iconSample;

	}

	public Icons525View genIcon525(Button btnComp, Icons525 icon525, Integer length){
		return genIcon525(btnComp,icon525,length,null,null);
	}

	public Icons525View genIcon525(Button btnComp, Icons525 icon525, Integer length, String fillColor, String strokeColor) {

		Icons525View iconSample = new Icons525View(icon525); //Icons525.EXCEL
		//iconSample.setGlyphStyle(String.format("-glyph-name:'%s';-fx-fill: %s;-fx-stroke:%s;-glyph-size: %dpx; ", name, fillColor, strokeColor, length));

		String format = "";

		if(length!=null)  format = String.format( format + "-glyph-size: %dpx;", length);

		if(fillColor!=null)format = String.format( format + " -fx-fill: %s;", fillColor);

		if(strokeColor!=null) format = String.format( format + " -fx-stroke: %s;", strokeColor);

		if(!format.equals(""))iconSample.setGlyphStyle(format);

		btnComp.setGraphic(iconSample);

		//iconSample.setStyle("-fx-fill: blue");
		//iconSample.setStyle("-icons-color: yellow");

		return iconSample;
	}

	public FontAwesomeIconView genIconFontAwesome(FontAwesomeIcon fontAwesomeIcon, Integer length, String fillColor, String strokeColor) {

		//Text layoutIcon = FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.TABLE);
		//EmojiOneView icon = new EmojiOneView(WeatherIcon._100);

		//Text icon2 = FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.ADDRESS_BOOK);

		FontAwesomeIconView iconExcel = new FontAwesomeIconView(fontAwesomeIcon);
		iconExcel.setGlyphStyle(String.format("-fx-fill: %s;-fx-stroke:%s;-glyph-size: %dpx; ", fillColor, strokeColor, length));

		return iconExcel;

	}


}
