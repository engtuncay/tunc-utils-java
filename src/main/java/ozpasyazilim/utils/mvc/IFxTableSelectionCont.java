package ozpasyazilim.utils.mvc;

/**
 * FxTableView2'de selectAndClose metodu için yapıldı.
 *
 * @param <EntClazz>
 */
public interface IFxTableSelectionCont<EntClazz> extends IFiModCont {

	// entitySelected
	EntClazz getEntitySelected();

	void setEntitySelected(EntClazz entitySelected);


}

