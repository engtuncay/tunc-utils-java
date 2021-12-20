package mark.utils.collections.filter;

public interface Filter<T> {
	public boolean accept(T obj);
}
