package ozpasyazilim.utils.table;

import javafx.scene.Node;
import javafx.scene.control.DatePicker;

import java.util.function.Supplier;

public class OzFieldFx {

	String header;
	Node node; // alternatif parent
	Supplier<Object> valueFunction;
	OzFieldType ozFieldType;

	public static OzFieldFx build(String header, OzFieldType colType) {
		OzFieldFx ozFieldFx = new OzFieldFx();
		ozFieldFx.setHeader(header);
		ozFieldFx.setOzFieldType(colType);
		return ozFieldFx;
	}



	//Taslak
	//OzTableCol ozTableCol;

	public static void main(String[] args) {

	}

	public Object getValue(){
		if(getValueFunction()!=null){
			return getValueFunction().get();
		}
		return  null;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public Supplier<Object> getValueFunction() {
		return valueFunction;
	}

	public void setValueFunction(Supplier<Object> valueFunction) {
		this.valueFunction = valueFunction;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public OzFieldType getOzFieldType() {
		return ozFieldType;
	}

	public void setOzFieldType(OzFieldType ozFieldType) {
		this.ozFieldType = ozFieldType;
	}

	public OzFieldFx autoField() {

		if(getOzFieldType()!=null && getOzFieldType()== OzFieldType.Date){
			DatePicker dtpField = new DatePicker();
			setNode(dtpField);
			setValueFunction(() -> {
				return dtpField.getValue();
			});

		}

		return this;
	}
}
