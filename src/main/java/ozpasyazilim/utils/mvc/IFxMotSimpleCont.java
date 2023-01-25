package ozpasyazilim.utils.mvc;

/**
 *
 * IFxSimpleCont'a ek olarak eklenecek (Mikro Projesindeki Metodlar için)
 *
 * @param <Viewclazz>
 */
public interface IFxMotSimpleCont<Viewclazz> extends IFxSimpleCont {
	IFxModView getModView();
	// Digerler IFxSimpleCont dan geliyor
}
