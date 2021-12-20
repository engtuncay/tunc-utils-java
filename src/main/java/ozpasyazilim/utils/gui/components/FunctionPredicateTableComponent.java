package ozpasyazilim.utils.gui.components;

import javax.swing.JTable;

public interface FunctionPredicateTableComponent {

	/**
	 * Evaluates this predicate on the given argument.
	 *
	 * @param t
	 *            the input argument
	 * @return {@code true} if the input argument matches the predicate,
	 *         otherwise {@code false}
	 */
	boolean test(JTable table, Object value, boolean isSelected, int row, int column);

}
