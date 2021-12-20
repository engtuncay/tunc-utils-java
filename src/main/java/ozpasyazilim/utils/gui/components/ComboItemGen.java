package ozpasyazilim.utils.gui.components;

/**
 * combo object item
 *
 * @author ttn
 */
public class ComboItemGen<EntClazz> {

	private String label;
	private EntClazz value;

	public ComboItemGen(String label, EntClazz value) {
		this.label = label;
		this.value = value;
	}

	public String toString() {
		return this.label;
	}

	public String getLabel() {
		return this.label;
	}

	public EntClazz getValue() {
		return this.value;
	}

	public void setLabel(String label) {
		this.label = label;
	}


}
