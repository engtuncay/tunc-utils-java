package ozpasyazilim.utils.core;

public class FiHtmlTag {

	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public FiHtmlTag bold() {
		this.setContent("<b>" + this.getContent() + "</b>");
		return this;
	}

	public static FiHtmlTag getInstance(String content) {
		FiHtmlTag fiHtmlTag = new FiHtmlTag(content);
		return fiHtmlTag;
	}

	private FiHtmlTag(String text) {
		this.content = text;
	}

	public String get() {
		return this.content;
	}

	public String toString() {
		return content;
	}

}
