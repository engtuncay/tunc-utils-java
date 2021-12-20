package ozpasyazilim.utils.gui.fxcomponents;

import org.reactfx.EventStreams;

import java.time.Duration;

public class FxTextFieldWitBtnLbl3<EntClazz> extends FxTextFieldBtn<EntClazz> {

	public FxTextFieldWitBtnLbl3() {
		lblAciklama = new FxLabel("");
		wrapFi();
		add(lblAciklama, "growx,pushx,span");

		// focus değişince
//		getFxTextField().focusedProperty().addListener((observable, oldValue, newValue) -> {
//			if (!newValue) {
//				getFxTextField().setTxValue(getFxTextField().getText());
//			}
//		});

		EventStreams.valuesOf(getFxTextField().textProperty())
				.successionEnds(Duration.ofMillis(100))
				.subscribe(s -> {
					getFxTextField().setTxValue(s);
				});

	}

}
