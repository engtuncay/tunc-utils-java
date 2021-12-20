package ozpasyazilim.utils.core;

/**
 * Wrapper şeklinde String metodlar
 *
 */
public class FiText {

	String text;

	public FiText() {
	}

	public FiText(String value) {
		this.text = value;
	}

	public static FiText bui() {
		return new FiText();
	}

	public static FiText bui(String value) {
		return new FiText(value);
	}

	/**
	 * Boşsa birleştirme yapmaz
	 *
	 * @param text
	 * @return
	 */
	public FiText combineWithColon(String text) {
		if (!FiString.isEmpty(text)) {
			if(FiString.isEmpty(getText())){
				setText(text);
			}else{
				setText(getText()+","+text);
			}
			return this;
		}
		return this;
	}

	public String getText() {
		return text;
	}

	public String getTextNotNull() {
		if(getText()==null) return "";
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
