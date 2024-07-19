package ozpasyazilim.utils.gui.fxcomponents;

import org.reactfx.EventStreams;

import java.time.Duration;

/**
 * TextField'ın Text Prop değeri, TxValue propuna anında iletilmeyip 100ms dan sonra iletilir.
 *
 * @param <EntClazz>
 */
public class FxTextFieldBtnWitLbl3<EntClazz> extends FxTextFieldBtn<EntClazz> {

    public FxTextFieldBtnWitLbl3() {
        lblAciklama = new FxLabel("");
        wrapFi();
        add(lblAciklama, "growx,pushx,span");

        // focus değişince
        // getFxTextField().focusedProperty().addListener((observable, oldValue, newValue) -> {
        // if (!newValue) {
        //  getFxTextField().setTxValue(getFxTextField().getText());
        // }
        //});

        EventStreams.valuesOf(getFxTextField().textProperty())
                .successionEnds(Duration.ofMillis(500))
                .subscribe(s -> {
                    getFxTextField().setTxValue(s);
                });

    }

}
