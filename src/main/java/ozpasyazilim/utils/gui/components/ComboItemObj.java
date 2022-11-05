package ozpasyazilim.utils.gui.components;

/**
 *
 * Combo box eklenecek item , value değeri gerçek kendi değerinde tutar.
 *
 * value gerçek değer olmalı,çevirime gerek duyulmamalı (string->int çevirme gibi)
 *
 * @author tunc270
 */
public class ComboItemObj {

	private String label;
	private Object value;

	public ComboItemObj() {
	}

	public ComboItemObj(Object value, String label) {
		this.label = label;
		this.value = value;
	}

	public String toString() {
		return this.label;
	}

	public String getLabel() {
		return this.label;
	}

	public Object getValue() {
		return this.value;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
