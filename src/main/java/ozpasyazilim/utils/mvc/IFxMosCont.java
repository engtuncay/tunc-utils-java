package ozpasyazilim.utils.mvc;

/**
 * Mos Controller Interface
 *
*/
public interface IFxMosCont<E> extends IFxSimpleCont, IFxSimpleGenCont<E> {

	IFxModView getModView();

}
