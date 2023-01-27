package ozpasyazilim.utils.gui.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**

 * Implementation of the Searchable interface that searches a List of String objects.

 * This implementation searches only the beginning of the words, and is not be optimized

 * for very large Lists.

 * @author G. Cope

 *

 */

public class ComboitemSearchable implements Searchable<ComboItemText,String>{



	private List<ComboItemText> terms = new ArrayList<ComboItemText>();



	/**

	 * Constructs a new object based upon the parameter terms.

	 * @param terms The inventory of terms to search.

	 */

	public ComboitemSearchable(List<ComboItemText> terms){

		this.terms.addAll(terms);

	}



	@Override

	public Collection<ComboItemText> search(String value) {

		List<ComboItemText> founds = new ArrayList<ComboItemText>();


		for ( ComboItemText s : terms ){

			if ( s.getLabel().indexOf(value) == 0 ){

				founds.add(s);

			}

		}

		return founds;

	}



}