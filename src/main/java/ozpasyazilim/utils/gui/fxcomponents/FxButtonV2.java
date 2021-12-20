package ozpasyazilim.utils.gui.fxcomponents;

import de.jensd.fx.glyphs.icons525.Icons525;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import ozpasyazilim.utils.security.SecurityRight;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FxButtonV2<EntClazz> extends Button implements IFxSecureNode {

	//Integer tiState;
	Boolean state = false;
	///UUID id = UUID.randomUUID();
	List<FxButtonV2> fxChildrenButtons;
	private EventHandler eventBtn;
	private String moduleCode;
	private ObjectProperty<EntClazz> propEntity;

	public FxButtonV2() {

	}

	public FxButtonV2(String text) {
		super(text);
	}

	public FxButtonV2(String text, String moduleCode) {
		super(text);
		this.moduleCode = moduleCode;
	}

	public FxButtonV2(String title, boolean autoHideChildren) {
		super(title);
		if (autoHideChildren) {
			setAutoHideChildren();
			getStyleClass().add("menubuttongrup");
			//setPrefWidth();
			setAlignment(Pos.TOP_LEFT);
		}
	}

	public FxButtonV2(String title, boolean autoHideChildren, Map<String, Object> propertiesBtnGroup) {
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

	public FxButtonV2(String text, Icons525 icon525) {
		super(text);
		setFxIcon(icon525);
	}

	public FxButtonV2(String text, String moduleCode, Set<String> userRights) {
		super(text);
		this.moduleCode = moduleCode;
		SecurityRight.checkSecurityRight(userRights,moduleCode,this);
	}

	public FxButtonV2(Icons525 ıcons525) {
		setFxIcon(ıcons525);
	}

	public FxButtonV2(Icons525 icon525, String txTooltip) {
		setFxIcon(icon525);
		setSimpleTooltip(txTooltip);
	}

	public FxButtonV2(String text, Icons525 icons525, String tooltip) {
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

	public List<FxButtonV2> getFxChildrenButtons() {
		if (fxChildrenButtons == null) {
			fxChildrenButtons = new ArrayList<>();
		}
		return fxChildrenButtons;
	}

	public void setFxChildrenButtons(List<FxButtonV2> fxChildrenButtons) {
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

	//public Integer getTiState() {return tiState;}

	//public void setTiState(Integer tiState) {this.tiState = tiState;}

	//	@Override
//	public boolean equals(Object obj) {
//		if(obj instanceof OfxButton) return id.equals( ((OfxButton)obj).id );
//		return super.equals(obj);
//	}


	public String getModuleCode() {
		return moduleCode;
	}

	@Override
	public void setNodeDisabledBySecurity(Boolean isDisabled) {
		if(isDisabled ==null) isDisabled =true;
		setDisable(isDisabled);
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public FxButtonV2 buildModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
		return this;
	}

	public void setSimpleTooltip(String text) {
		setTooltip(new Tooltip(text));
	}

	public void makeSecureButton(ObjectProperty<Set<String>> setObjectProperty) {

		Boolean hasSecurityRight = SecurityRight.checkSecurityRight(setObjectProperty.get(), getModuleCode(), this);

		setObjectProperty.addListener((observable, oldValue, newValue) -> {
			SecurityRight.checkSecurityRight(newValue,getModuleCode(),this);
		});

	}

	public void makeSecureButton2(ObjectProperty<SecurityRight> securityRightProperty) {

		SecurityRight.checkSecurityRight(securityRightProperty.get(), getModuleCode(),this);

		securityRightProperty.addListener((observable, oldValue, newValue) -> {
			SecurityRight.checkSecurityRight(newValue,getModuleCode(),this);
		});

//		securityRightProperty.get().userRightsProperty().addListener((observable, oldValue, newValue) -> {
//			SecurityRight.checkSecurityRight(newValue,getModuleCode(),this);
//		});

	}

	public ObjectProperty<EntClazz> propEntityProperty() {
		if(propEntity==null) {
			propEntity=new SimpleObjectProperty<>();
		}
		return propEntity;
	}

	public EntClazz getPropEntity() {
		if(propEntity ==null) propEntityProperty();
		return propEntity.get();
	}

	public void setPropEntity(EntClazz propEntity) {
		if(propEntity==null) propEntityProperty();
		this.propEntity.set(propEntity);
	}
}
