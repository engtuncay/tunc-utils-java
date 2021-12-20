package ozpasyazilim.utils.gui.components;

public class Tuplethree<R, S, T> {

	public final R first;
	public final S second;
	public final T third;

	public Tuplethree(R first, S second, T third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	public R getFirst() {
		return first;
	}

	public S getSecond() {
		return second;
	}

	public T getThird() {
		return third;
	}

}
