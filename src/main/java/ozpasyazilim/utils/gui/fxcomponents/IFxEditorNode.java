package ozpasyazilim.utils.gui.fxcomponents;

public interface IFxEditorNode {

	public String getFieldName();

	/**
	 * Entity hangi alanını kullanıyorsa , alanın ismi set edilir.
	 *
	 * @param fieldName
	 */
	public void setFieldName(String fieldName);

	String getFxId();
	void setFxId(String fxId);

}
