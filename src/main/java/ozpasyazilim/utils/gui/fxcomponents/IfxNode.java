package ozpasyazilim.utils.gui.fxcomponents;

import ozpasyazilim.utils.annotations.FiExpiremental;
import ozpasyazilim.utils.table.OzColType;

/**
 * Comp ler ilgili ortak metodları buraya yazılacak
 *
 */
public interface IfxNode {

	Object getCompValue();
	void setCompValue(Object objValue);

	OzColType getCompValueType();

	void setCompValueType(OzColType ozColType);

	Boolean getBoInvalidData();

	Object getCompValueByColType();

	@Deprecated
	@FiExpiremental // sonradan kaldırılacak , coltype getCompValueType dan alıacak
	Object getCompValueByColType(OzColType ozColType);

}
