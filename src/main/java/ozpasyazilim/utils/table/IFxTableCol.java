package ozpasyazilim.utils.table;

public interface IFxTableCol<EntClazz> {

	FiCol<EntClazz> getRefFiCol();

	void setRefFiCol(FiCol<EntClazz> fiTableCol);

}
