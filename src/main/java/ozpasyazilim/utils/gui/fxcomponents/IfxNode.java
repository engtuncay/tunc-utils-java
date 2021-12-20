package ozpasyazilim.utils.gui.fxcomponents;

import ozpasyazilim.utils.mvcanno.FiExpiremental;
import ozpasyazilim.utils.table.OzColType;

/**
 * Comp ler ilgili ortak metodları buraya yazılacak
 *
 */
public interface IfxNode {

	Object getCompValue();
	void setCompValue(Object objValue);
	OzColType getCompValueType();
	Boolean getBoInvalidData();
	@FiExpiremental // sonradan kaldırılacak , coltype getCompValueType dan alıacak
	Object getCompValueByColType(OzColType ozColType);

}
