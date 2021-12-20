package ozpasyazilim.utils.gui.fxcomponents;

import de.jensd.fx.glyphs.icons525.Icons525;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import ozpasyazilim.utils.core.FiThread;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.security.SecurityRight;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FxButton extends Button implements IFxSecureNode {

	Boolean state = false;
	//UUID id = UUID.randomUUID();
	List<FxButton> fxChildrenButtons;
	private EventHandler eventBtn;
	private String moduleCode;
	//button da bir state tutmak için eklendi (mod ile belli bir sayıda çevirmek için int yapıldı)
	private IntegerProperty lnState;
	// statelerde farklı text özelliği sunmak için
	private List<String> stateTextList;
	private Integer lnStateMod;
	private String txDefaultText;
	private String txValue;
	private Integer lnIdValue;

	//private ObjectProperty<EntClazz> propValue;

	public FxButton() {

	}

	public FxButton(String text) {
		super(text);
	}

	public FxButton(String text, String moduleCode) {
		super(text);
		this.moduleCode = moduleCode;
	}

	public FxButton(String title, boolean autoHideChildren) {
		super(title);
		if (autoHideChildren) {
			setAutoHideChildren();
			getStyleClass().add("menubuttongrup");
			//setPrefWidth();
			setAlignment(Pos.TOP_LEFT);
		}
	}

	public FxButton(String title, boolean autoHideChildren, Map<String, Object> propertiesBtnGroup) {
		super(title);
		setAlignment(Pos.TOP_LEFT);

		if (autoHideChildren) {
			setAutoHideChildren();
		}

		if (propertiesBtnGroup.containsKey("styleClass")) {
			String styleclass = (String) propertiesBtnGroup.get("styleClass");
			getStyleClass().add(styleclass);
		}

		if (propertiesBtnGroup.containsKey("width")) {
			Double width = (Double) propertiesBtnGroup.get("width");
			setPrefWidth(width);
		}

	}

	public FxButton(String text, Icons525 icon525) {
		super(text);
		setFxIcon(icon525);
	}



	public FxButton(String text, String moduleCode, Set<String> userRights) {
		super(text);
		this.moduleCode = moduleCode;
		SecurityRight.checkSecurityRight(userRights, moduleCode, this);
	}

	public FxButton(Icons525 ıcons525) {
		setFxIcon(ıcons525);
	}

	public FxButton(Icons525 icon525, String txTooltip) {
		setFxIcon(icon525);
		setSimpleTooltip(txTooltip);
	}

	public FxButton(String text, Icons525 icons525, String tooltip) {
		super(text);
		setFxIcon(icons525);
		setSimpleTooltip(tooltip);
	}


	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

	public List<FxButton> getFxChildrenButtons() {
		if (fxChildrenButtons == null) {
			fxChildrenButtons = new ArrayList<>();
		}
		return fxChildrenButtons;
	}

	public void setFxChildrenButtons(List<FxButton> fxChildrenButtons) {
		this.fxChildrenButtons = fxChildrenButtons;
	}

	public void setAutoHideChildren() {

		eventBtn = event -> {
			//Loghelperr.getInstance(getClass()).debug("btnList Tıklandı");
			//OfxButton btn = (OfxButton) event.getSource();
			//List<OfxButton> listBtn = btn.getFxChildrenButtons();
			this.setState(!getState());
			for (Button btnS : fxChildrenButtons) {
				btnS.setVisible(getState());
				btnS.setManaged(getState());
			}
		};
		this.setOnAction(eventBtn);

	}

	public void setAutoVisibleFirstState() {

		for (Button btnS : fxChildrenButtons) {
			btnS.setVisible(getState());
			btnS.setManaged(getState());
		}

	}

	public void setFxIcon(Icons525 icon) {
		new FxAwesomeIcon().genIcon525(this, icon, 12, "white", null);
	}

	public String getModuleCode() {
		return moduleCode;
	}

	@Override
	public void setNodeDisabledBySecurity(Boolean isDisabled) {
		if (isDisabled == null) isDisabled = true;
		setDisable(isDisabled);
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public FxButton buildModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
		return this;
	}

	public void setSimpleTooltip(String text) {
		setTooltip(new Tooltip(text));
	}

	public List<String> getStateTextList() {
		return stateTextList;
	}

	public void setStateTextList(List<String> stateTextList) {
		this.stateTextList = stateTextList;
	}

	public Integer getLnStateMod() {
		return lnStateMod;
	}

	public void setLnStateMod(Integer lnStateMod) {
		this.lnStateMod = lnStateMod;
	}

	public void activateState(Integer firstState) {

		lnStateProperty().addListener((observable, oldValue, newValue) -> {

			if (getStateTextList() != null && getLnStateMod()!=null) {
				int index = (Integer) newValue % getLnStateMod();
				if (getStateTextList().get(index) != null) {
					Platform.runLater(() -> {
						setText(getStateTextList().get(index));
					});
				}
			}

		});

		if (firstState != null) {
			setLnState(firstState);
		}

	}

	public IntegerProperty lnStateProperty() {
		if (lnState == null) {
			lnState = new SimpleIntegerProperty();
		}
		return lnState;
	}

	public void setLnState(Integer lnState) {
		lnStateProperty().set(lnState);
	}

	public int getLnState() {
		if (lnState == null) lnStateProperty();
		return lnState.get();
	}

	public Integer getLnStateWithMod() {
		if (lnState == null) return null;

		if (lnStateMod != null) {
			return getLnState() % getLnStateMod();
		}

		return lnState.get();
	}

	public void changeLnState() {
		if(getLnStateMod()!=null){
			//Loghelper.debug(getClass(), "State Değişti");
			setLnState((getLnState() + 1) % getLnStateMod());
		}else{
			FxDialogShow.showPopError("İşlem yapılamadı.");
		}
	}

	public void setOnActionWithThread(Runnable runnable) {
		setOnAction(event -> FiThread.startThread(runnable,this));
	}

	public String getTxDefaultText() {
		return txDefaultText;
	}

	public void setTxDefaultText(String txDefaultText) {
		this.txDefaultText = txDefaultText;
	}

	public String getTxValue() {
		return txValue;
	}

	public void setTxValue(String txValue) {
		this.txValue = txValue;
	}

	public Integer getLnIdValue() {
		return lnIdValue;
	}

	public void setLnIdValue(Integer lnIdValue) {
		this.lnIdValue = lnIdValue;
	}

}
