package ozpasyazilim.utils.gui.components;

/**
 * A method that wants to return multiple values would then be declared as follows:
 * <p>
 * public void methodThatReturnsTwoValues(Returnparam<ClassA> nameForFirstValueToReturn, Returnparam
 * <ClassB> nameForSecondValueToReturn) {
 * //...
 * nameForFirstValueToReturn.set("...");
 * nameForSecondValueToReturn.set("...");
 * //...
 * }
 * <p>
 * (for primitive datatypes I use minor variations to directly store the value)
 *
 * @param <T> return object
 * @author stackoverlow
 */
public class ReturnParam<T> {

	private T value;

	public ReturnParam() {
		this.value = null;
	}

	public ReturnParam(T initialValue) {
		this.value = initialValue;
	}

	public void set(T value) {
		this.value = value;
	}

	public T get() {
		return this.value;
	}
}