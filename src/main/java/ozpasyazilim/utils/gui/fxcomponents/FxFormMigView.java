package ozpasyazilim.utils.gui.fxcomponents;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.tbee.javafx.scene.layout.MigPane;
import ozpasyazilim.utils.core.FiBoolean;
import ozpasyazilim.utils.mvc.IFxModView;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.table.FiColInfHelper;

import java.util.List;

public class FxFormMigView extends FxMigPane implements IFxModView {

	HBox hboxToolbar;
	MigPane migForm;
	Boolean isEditable;
	List<IFiCol> listFormElements;

	public FxFormMigView(List<IFiCol> listFormElements) {
		super("insets 0,fill", "0[grow]", "0[]2[]");

		setListFormElements(listFormElements);

		migForm = new MigPane("insets 5", "0[]", "5[]");
		//migForm.setPadding(new Insets(0,0,0,0));

		setEditable(true);

		listFormElements.forEach(infTableCol -> {

			if (FiBoolean.isTrue(infTableCol.getBoHidden())) {
				return;
			}

			//if(!FiBoolean.isTrue(infTableCol.getHidden())){
			Label lblForm = new Label(infTableCol.getHeaderName());
			migForm.add(lblForm, "width 100");
			Node node = FxEditorFactory.generateAndSetFilterNode(infTableCol);
			node.setDisable(FiBoolean.isNullOrFalse(isEditable));
			migForm.add(node, String.format("width %s,wrap","300"));
			//}

		});

		hboxToolbar = new FxHBox();

		add(migForm, "wrap");  //span,gap 0
		add(hboxToolbar, "wrap,pushx,growx");

	}

	public static void setFieldsAllToRequiredFi(List<? extends IFiCol> listColumn) {
		listColumn.forEach(o -> {
			o.setBoRequired(true);
		});
	}


	public HBox getHboxToolbar() {
		return hboxToolbar;
	}

	public void setHboxToolbar(HBox hboxToolbar) {
		this.hboxToolbar = hboxToolbar;
	}

//	@Override
//	public Parent getView() {
//		return this;
//	}

//	@Override
//	public FxMigPane getRootMigPane() {
//		return this;
//	}
//
//	@Override
//	public FxStackPane getRootStackPane() {
//		return null;
//	}

	@Override
	public Pane getRootPane() {
		return this;
	}

	@Override
	public void initGui() {

	}

	public Boolean getEditable() {
		return isEditable;
	}

	public void setEditable(Boolean editable) {
		isEditable = editable;
		toggleEditable(editable);
	}

	public void toggleEditable(Boolean editable) {
		this.isEditable = editable;
	}

	public Node getCompByFieldName(String toString) {
		return FiColInfHelper.build(getListFormElements()).findColumnByFieldName(toString).getColFilterNode();
	}

	public IFiCol getColByFieldName(String toString) {
		return FiColInfHelper.build(getListFormElements()).findColumnByFieldName(toString);
	}

	public List<IFiCol> getListFormElements() {
		return listFormElements;
	}

	public void setListFormElements(List<IFiCol> listFormElements) {
		this.listFormElements = listFormElements;
	}

	public Object getEntity(Class clazz) {
		return new FxEditorFactory().bindFormToEntityByFilterNode(getListFormElements(), clazz);
	}
}


