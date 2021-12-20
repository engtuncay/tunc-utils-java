package ozpasyazilim.utils.core;

public interface FiFormatter {

	/**
	 * Convert a object to String.
	 */
	public abstract String format(Object obj);

	/**
	 * Convert the String to the Object.
	 */
	public abstract Object parse(Object s);

	/**
	 * Naming proposes only
	 */
	public abstract String getName();

}
