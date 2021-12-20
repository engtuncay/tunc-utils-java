package ozpasyazilim.utils.functions;

@FunctionalInterface
public interface FourConsumer<A, B, C, D> {

    void consume(A a, B b, C c, D d);

    // R consume(A a, B b, C c,D d);
    // default <V> FourConsumer<A, B, C, D> andThen(Function<? super R, ? extends V>
    // after) {
    // Objects.requireNonNull(after);
    // return (A a, B b, C c,D d) -> after.apply(apply(a, b, c,d));
    // }

}
