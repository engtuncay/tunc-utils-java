package ozpasyazilim.utils.gui.components;

/**
 * combobox value becomes return String of tostring method
 *
 *
 * @author ttn
 *
 */

public class ComboObject<E> {
	private String label;

	private E value;

	public ComboObject(String label, E value) {
		this.label = label;
		this.value = value;
	}

	public String toString() {
		return this.label;
	}

	public String getLabel() {
		return this.label;
	}

	public E getValue() {

		return this.value;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
