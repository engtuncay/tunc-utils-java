package ozpasyazilim.utils.gui.fxcomponents;

import ozpasyazilim.utils.annotations.FiExpiremental;
import ozpasyazilim.utils.table.OzColType;

/**
 * Custom componentlerde kullanılacak ortak metodlar
 * <p>
 * Örnek Custom Componentler {@link FxTextFieldBtn}
 * <p>
 * IFxNode interface kullanan bir component ise degeri kendi metodundan alınır.
 * <p>
 * {@code ifxNode1.getCompValueByColType(ozColType) }
 */
public interface IfxNode {

    Object getCompValue();

    void setCompValue(Object objValue);

    OzColType getCompValueType();

    void setCompValueType(OzColType ozColType);

    Boolean getBoInvalidData();

    /**
     * OzColType göre çevrim yaparak döndürür
     *
     * @return
     */
    Object getCompValueByColType();

    /**
     * sonradan kaldırılacak , coltype getCompValueType dan alıacak
     *
     * @param ozColType
     * @return
     */
    @Deprecated
    @FiExpiremental
    Object getCompValueByColType(OzColType ozColType);

}
