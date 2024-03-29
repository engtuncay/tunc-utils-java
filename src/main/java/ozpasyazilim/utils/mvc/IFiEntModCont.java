package ozpasyazilim.utils.mvc;

/**
 * IFxSimpleCont'a ek olarak eklenecek (Mikro Projesindeki Metodlar için)
 * <p>
 * IFxSimpleCont Interface : void initCont(),IFxSimpleView getModView(),Stage getFxStage(),void setFxStage(...)
 * <p>
 * String getModuleCode(),String getModuleLabel(),String getCloseReason(),void setCloseReason(...);
 *
 * @param  //<Viewclazz> kaldırıldı
 */
public interface IFiEntModCont extends IFiModCont {

    /**
     * IFxEntSimpleCont ile IFxSimpleView aynı
     * @return
     */
    IFiModView getModView();
    // Digerler IFxSimpleCont dan geliyor
}
