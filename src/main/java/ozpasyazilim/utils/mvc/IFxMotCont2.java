package ozpasyazilim.utils.mvc;

/**
 * Viewclazz view sınıfını göstermek için konuldu. bilgi amaçlı. İşlevi yok.
 *
 * Aynı işlevdeki IFxSimpleCont kullanılmalı
 *
 * Use IFxMotCont or IFxSimpleCont
 *
 * @param <Viewclazz>
 */
@Deprecated
public interface IFxMotCont2<Viewclazz> extends IFxSimpleCont {
	IFxModView getModView();
	// Digerler IFxSimpleCont dan geliyor
}
