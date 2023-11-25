package ozpasyazilim.utils.gui.fxcomponents;

import javafx.scene.control.Label;
import org.tbee.javafx.scene.layout.MigPane;
import ozpasyazilim.utils.core.FiBool;
import ozpasyazilim.utils.mvc.IFiCol;

import java.util.List;

public class FxFormFactory {

	public void setupPlainForm(MigPane migForm1, List<IFiCol> listFormElements){

		listFormElements.forEach( ozTableCol -> {

			if(!FiBool.isTrue(ozTableCol.getBoHidden())){
				Label lblForm = new Label(ozTableCol.getHeaderName());
				migForm1.add(lblForm,"width 100");
				migForm1.add(new FxEditorFactory().generateAndSetFilterNode(ozTableCol),"wrap");


			}

		});

	}


}
