package ozpasyazilim.utils.fxwindow;

import javafx.scene.Node;
import ozpasyazilim.utils.core.FiBool;
import ozpasyazilim.utils.gui.fxcomponents.FxDialogShow;
import ozpasyazilim.utils.gui.fxcomponents.FxMigHp;
import ozpasyazilim.utils.gui.fxcomponents.FxMigPaneView;
import ozpasyazilim.utils.gui.fxcomponents.FxWindow;
import ozpasyazilim.utils.mvc.AbsFiModBaseCont;
import ozpasyazilim.utils.mvc.IFxTableSelectionCont;

public class FxSimpleContGen<E> extends AbsFiModBaseCont implements IFxTableSelectionCont<E> {

	FxMigPaneView modView;
	// Entity neden gerek duyuldu ???
	E entitySelected;

	public FxSimpleContGen() {

	}

	public FxSimpleContGen(Boolean withInit) {
		if (FiBool.isTrue(withInit)) {
			initCont();
		}
	}

	@Override
	public void initCont() {
		modView = new FxMigPaneView(FxMigHp.bui().lcgInset3Gap33().lcgNoGrid().getLcg());
	}


	@Override
	public FxMigPaneView getModView() {
		return modView;
	}

	public void openAsDialogSync(Node nodeRelative, Boolean boNonModal) {
		openAsDialogSync(nodeRelative,boNonModal,null, null);
	}

	/**
	 * Farklı Thread içinde yapılıyorsa platform runlater içinde kullanılmalı
	 */
	public void openAsDialogSync(Node nodeRelative, Boolean boNonModal, Integer width, Integer height) {
		FxDialogShow fxDialogShow = new FxDialogShow();

		if (getModView() == null) {
			initCont();
		}

		getModView().getRootPane().getStylesheets().add("app.css");
		FxWindow.nodeWindow(nodeRelative, this, width, height, boNonModal);
	}

	@Override
	public E getEntitySelected() {
		return entitySelected;
	}

	@Override
	public void setEntitySelected(E entitySelected) {
		this.entitySelected = entitySelected;
	}


}
