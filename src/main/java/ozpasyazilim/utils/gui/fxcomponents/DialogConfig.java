package ozpasyazilim.utils.gui.fxcomponents;

import javafx.scene.Node;

public class DialogConfig {

	String txContent;
	String title;
	Double width;
	Double height;
	Boolean boNonModal;
	Node nodeRelative;
	String cssFileName;

	//Double nmPrefWidth;

	public DialogConfig() {
	}

	public DialogConfig(String txContent) {
		this.txContent = txContent;
	}

	public static DialogConfig factory() {
		return new DialogConfig();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public DialogConfig buildTitle(String title) {
		setTitle(title);
		return this;
	}

	public DialogConfig buiContent(String txContent) {
		setTxContent(txContent);
		return this;
	}

	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public DialogConfig buildWidth(double v) {
		setWidth(v);
		return this;
	}

	public String getTxContent() {
		return txContent;
	}

	public void setTxContent(String txContent) {
		this.txContent = txContent;
	}

	public Boolean getBoNonModal() {return boNonModal;}

	public void setBoNonModal(Boolean boNonModal) {this.boNonModal = boNonModal;}

	public Node getNodeRelative() {return nodeRelative;}

	public void setNodeRelative(Node nodeRelative) {this.nodeRelative = nodeRelative;}

	public String getCssFileName() {
		return cssFileName;
	}

	public void setCssFileName(String cssFileName) {
		this.cssFileName = cssFileName;
	}


}
