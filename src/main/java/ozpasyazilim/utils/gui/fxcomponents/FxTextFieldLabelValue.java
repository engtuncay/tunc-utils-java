package ozpasyazilim.utils.gui.fxcomponents;

/**
 * Bu textfield da görünen text ile değer ayrıştırılır
 * gerçek değer TxValue 'ya, görünecek değerde ise text e yazılır.
 *
 * @param <EntClazz>
 */
public class FxTextFieldLabelValue<EntClazz> extends FxTextField<EntClazz> {

	public FxTextFieldLabelValue(String text) {
		super(text);
	}

	public FxTextFieldLabelValue() {
	}
}
