package ozpasyazilim.utils.core;

public interface IFiParser {

    /**
     * Convert a object to String.
     * <p>
     * object => string
     */
    public String deParse(Object obj);

    /**
     * Convert the String to the Object.
     * <p>
     * string => object
     */
    public Object parse(Object s);

    // Eklenme amacı nedir ????
//    /**
//     * Naming proposes only
//     */
    //public abstract String getName();

}
