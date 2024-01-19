package ozpasyazilim.utils.gui.components;

/**
 * toString metodu label değişkenini (özelliğini) döndürür.
 * <p>
 * ToString method returns label property.
 *
 * @author torak
 */
public class ComboItemText {

	private String label;
	private String value;

	// Extra fields
	private Runnable onAction;
	private String txCode;
	private String txSpec1;

	public ComboItemText(String label, String value) {
		this.label = label;
		this.value = value;
	}

	public ComboItemText(String label, String value, String txSpec1) {
		this.label = label;
		this.value = value;
		this.txSpec1 = txSpec1;
	}

	public ComboItemText(String label) {
		this.label = label;
		//this.onAction = onAction;
	}

//	public ComboItem() {
//	}

	public static ComboItemText build(Object objLabel, Object objValue) {
		ComboItemText comboItem = new ComboItemText(objLabel.toString(), objValue.toString());
		return comboItem;
	}

	public static ComboItemText buildWitAction(String label, Runnable onAction) {
		ComboItemText comboItem = new ComboItemText(label);
		comboItem.setOnAction(onAction);
		return comboItem;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj == null) return false;

		if (obj.getClass() != this.getClass()) {

			if (obj instanceof String) {
				if (getValue() == null) return false;

				if (getValue().equals((String) obj)) {
					return true;
				}
			}
			return false;
		}

		ComboItemText comboItem = (ComboItemText) obj;

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

	public ComboItemText buildOnAction(Runnable onAction) {
		this.onAction = onAction;
		return this;
	}

	public String getTxCode() {
		return txCode;
	}

	public ComboItemText setTxCode(String txCode) {
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
