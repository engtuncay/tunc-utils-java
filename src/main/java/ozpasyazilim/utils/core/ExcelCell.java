package ozpasyazilim.utils.core;

import java.util.HashMap;
import java.util.Map;

public class ExcelCell {

	public Object value;

	Map<CellStyles,String> styleMap;

	public ExcelCell() {
	}

	public ExcelCell(Object cellValue, Map<CellStyles, String> styleMap) {
		setValue(cellValue);
		setStyleMap(styleMap);
	}

	public ExcelCell(Object cellValue) {
		setValue(cellValue);
	}

	public enum CellStyles {fontSize, alignment }

	public enum StyleAlignments { left,right,center }
	public enum StyleFontSize { big }

	public Map<CellStyles, String> getStyleMap() {
		if (styleMap == null) {
			styleMap = new HashMap<>();
		}
		return styleMap;
	}

	public void setStyleMap(Map<CellStyles, String> styleMap) {
		this.styleMap = styleMap;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}


}
