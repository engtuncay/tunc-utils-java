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
public interface IFxEntSimpleCont extends IFxSimpleCont {

    /**
     * IFxModView , IFxSimpleView ile aynı
     * @return
     */
    IFxEntSimpleView getModView();
    // Digerler IFxSimpleCont dan geliyor
}
