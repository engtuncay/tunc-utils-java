package ozpasyazilim.utils.gui.fxcomponents;

import javafx.scene.Node;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.table.IFiColHelper;

import java.util.List;

public class FxForm {

	List<? extends IFiCol> listElements;

	public FxForm(List<? extends IFiCol> listFormElements) {
		setListElements(listFormElements);
	}

	public List<? extends IFiCol> getListElements() {
		return listElements;
	}

	public void setListElements(List<? extends IFiCol> listElements) {
		this.listElements = listElements;
	}

	public Node getEditorComp(String fieldName) {

		IFiCol ozTableCol = IFiColHelper.build(listElements).getIFiColByID(fieldName);
		return ozTableCol.getColFilterNode();

	}

	public FxTextField getEditorCompFxTexfield(String fieldName) {

		IFiCol ozTableCol = IFiColHelper.build(listElements).getIFiColByID(fieldName);

		if (ozTableCol.getFilterNodeClass().equals(FxTextField.class.getName())) {
			FxTextField comp = (FxTextField) ozTableCol.getColFilterNode();
			return comp;
		}

		return null;
	}

	public FxDatePicker getEditorCompFxDatePicker(String fieldName) {

		IFiCol ozTableCol = IFiColHelper.build(listElements).getIFiColByID(fieldName);

		if (ozTableCol.getFilterNodeClass().equals(FxDatePicker.class.getName())) {
			FxDatePicker comp = (FxDatePicker) ozTableCol.getColFilterNode();
			return comp;
		}

		return null;
	}
}
