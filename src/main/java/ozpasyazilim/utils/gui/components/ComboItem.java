package ozpasyazilim.utils.gui.components;

/**
 * combobox value becomes return String of tostring method
 *
 * @author ttn
 */
public class ComboItem {

	private String label;
	private String value;
	private Runnable onAction;
	private String txCode;
	private String txSpec1;

	public ComboItem(String label, String value) {
		this.label = label;
		this.value = value;
	}

	public ComboItem(String label, String value,String txSpec1) {
		this.label = label;
		this.value = value;
		this.txSpec1 = txSpec1;
	}

	public ComboItem(String label) {
		this.label = label;
		//this.onAction = onAction;
	}

//	public ComboItem() {
//	}

	//	public ComboItem(Runnable onAction,String label) {
//		this.label = label;
//		this.onAction = onAction;
//	}

	public static ComboItem build(Object objLabel, Object objValue) {
		ComboItem comboItem = new ComboItem(objLabel.toString(), objValue.toString());
		return comboItem;
	}

	public static ComboItem buildWitAction(String label, Runnable onAction) {
		ComboItem comboItem = new ComboItem(label);
		comboItem.setOnAction(onAction);
		return comboItem;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if(obj==null) return false;

		if (obj.getClass() != this.getClass()) {

			if(obj instanceof String){
				if(getValue()==null) return false;

				if (getValue().equals((String)obj)) {
					return true;
				}
			}
			return false;
		}

		ComboItem comboItem = (ComboItem) obj;

		if (comboItem.getValue() == null || getValue() == null) {
			return false;
		}

		if (getValue().equals(comboItem.getValue())) {
			return true;
		}

		return false;
	}

	public String toString() {
		return this.label;
	}

	public String getLabel() {
		return this.label;
	}

	public String getValue() {

		return this.value;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Runnable getOnAction() {
		return onAction;
	}

	public void setOnAction(Runnable onAction) {
		this.onAction = onAction;
	}

	public ComboItem buildOnAction(Runnable onAction) {
		this.onAction = onAction;
		return this;
	}

	public String getTxCode() {
		return txCode;
	}

	public ComboItem setTxCode(String txCode) {
		this.txCode = txCode;
		return this;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTxSpec1() {
		return txSpec1;
	}

	public void setTxSpec1(String txSpec1) {
		this.txSpec1 = txSpec1;
	}
}
