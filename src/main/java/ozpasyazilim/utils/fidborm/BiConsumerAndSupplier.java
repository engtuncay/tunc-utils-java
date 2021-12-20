package ozpasyazilim.utils.fidborm;

@FunctionalInterface
public interface BiConsumerAndSupplier<T, U, S> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     */
    S accept(T t, U u);
}