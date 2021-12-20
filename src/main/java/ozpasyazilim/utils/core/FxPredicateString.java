package ozpasyazilim.utils.core;

import java.util.function.Predicate;

public class FxPredicateString {

	Predicate<String> predicate;

	public FxPredicateString(Predicate<String> predicate) {
		this.predicate = predicate;
	}

	public Predicate<String> getPredicate() {
		return predicate;
	}

	public void setPredicate(Predicate<String> predicate) {
		this.predicate = predicate;
	}
}
