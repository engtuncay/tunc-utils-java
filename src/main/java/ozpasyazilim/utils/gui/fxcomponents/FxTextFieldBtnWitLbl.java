package ozpasyazilim.utils.gui.fxcomponents;

import org.reactfx.EventStreams;
import ozpasyazilim.utils.annotations.FiDraft;

import java.time.Duration;

/**
 *
 *
 * @param
 */
@FiDraft
@Deprecated
public class FxTextFieldBtnWitLbl extends FxTextFieldBtn<Object> {

    public FxTextFieldBtnWitLbl() {
        lblAciklama = new FxLabel("");
        wrapFi();
        add(lblAciklama, "growx,pushx,span");

        // focus değişince
        // getFxTextField().focusedProperty().addListener((observable, oldValue, newValue) -> {
        // if (!newValue) {
        //  getFxTextField().setTxValue(getFxTextField().getText());
        // }
        //});

//        EventStreams.valuesOf(getFxTextField().textProperty())
//                .successionEnds(Duration.ofMillis(500))
//                .subscribe(s -> {
//                    getFxTextField().setTxValue(s);
//                });
//
//    }
    }

}
