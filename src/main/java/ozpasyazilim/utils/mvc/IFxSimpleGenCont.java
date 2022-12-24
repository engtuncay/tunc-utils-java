package ozpasyazilim.utils.mvc;

public interface IFxSimpleGenCont<E> extends IFxSimpleCont {

	E getEntitySelected();
	void setEntityDefault(E entitySelected);

}

