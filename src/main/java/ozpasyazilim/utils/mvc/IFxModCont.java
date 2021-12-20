package ozpasyazilim.utils.mvc;

/**
 * Viewclazz view sınıfını göstermek için sadece
 *
 * @param <Viewclazz>
 */
public interface IFxModCont<Viewclazz> extends IFxSimpleCont { //Viewclazz extends IFxModView

	// Burada viewclazz çıkarıldı.
	//default IFxModView getModView() {return null;}
	IFxModView getModView();

	// Digerler IFxSimpleCont dan geliyor

}
