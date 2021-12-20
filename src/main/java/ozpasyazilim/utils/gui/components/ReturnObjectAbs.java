package ozpasyazilim.utils.gui.components;

/**
 * Abstract Return model object
 * 
 * 
 * @author TUNC
 *
 * @param <E>
 */
public interface ReturnObjectAbs<E> {

	public Boolean getResult();

	public void setResult(Boolean success);

	public E getReturnObject();

	public void setObject(E e);

	public void setMessageSpecial(String message);

	public String getMessageSpecial();

	public void setErrorMessage(String error);

	public String getErrorMessage();

}
