package ozpasyazilim.utils.gui.fxcomponents;

import javafx.scene.Node;
import ozpasyazilim.utils.core.FiBoolean;
import ozpasyazilim.utils.mvc.AbsFxSimpleCont;
import ozpasyazilim.utils.mvc.IFxSimpleGenCont;

public class FxSimpleContGen<E> extends AbsFxSimpleCont implements IFxSimpleGenCont<E> {

	FxMigPaneView modView;
	// Entity neden gerek duyuldu ???
	E entityDefault;

	public FxSimpleContGen() {

	}

	public FxSimpleContGen(Boolean withInit) {
		if (FiBoolean.isTrue(withInit)) {
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
		fxDialogShow.nodeModalByIFxSimpleCont(nodeRelative, this, width, height, boNonModal);
	}

	@Override
	public E getEntitySelected() {
		return entityDefault;
	}

	@Override
	public void setEntityDefault(E entitySelected) {
		this.entityDefault = entitySelected;
	}


}
