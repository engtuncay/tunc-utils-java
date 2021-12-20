package ozpasyazilim.utils.gui.components;

/**
 * combobox value becomes return String of tostring method
 *
 *
 * @author ttn
 *
 */


public class ComboitemInt {

	private String label;
	private Integer value;

	public ComboitemInt(String label, Integer intvalue) {
		this.label = label;
		this.value = intvalue;
	}

	public String toString() {
		return this.label;
	}

	public String getLabel() {
		return this.label;
	}

	public Integer getValue() {

		return this.value;
	}

	public void setLabel(String label) {
		this.label = label;
	}


}
