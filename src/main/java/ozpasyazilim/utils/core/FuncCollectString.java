package ozpasyazilim.utils.core;

/**
 * bunun yerine funtion T,String kullanalabilir
 * @param <E>
 */
@Deprecated
@FunctionalInterface
public interface FuncCollectString<E> {

	public String getValue(E e);
}
