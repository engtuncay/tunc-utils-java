package ozpasyazilim.utils.mvc;

/**
 * Viewclazz view sınıfını göstermek için konuldu. bilgi amaçlı. İşlevi yok.
 *
 * Aynı işlevdeki IFxSimpleCont kullanılmalı
 *
 * @param <Viewclazz>
 */
public interface IFxModCont<Viewclazz> extends IFxSimpleCont {
	IFxModView getModView();
	// Digerler IFxSimpleCont dan geliyor
}
